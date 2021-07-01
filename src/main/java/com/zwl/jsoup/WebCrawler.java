package com.zwl.jsoup;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.zwl.jsoup.mapper.AnswerMapper;
import com.zwl.jsoup.mapper.TopicMapper;
import com.zwl.jsoup.model.Answer;
import com.zwl.jsoup.model.AnswerEvent;
import com.zwl.jsoup.model.DomParseEvent;
import com.zwl.jsoup.model.ParseDTO;
import com.zwl.jsoup.model.QueryAnswerEvent;
import com.zwl.jsoup.model.Topic;
import com.zwl.jsoup.thread.CrawlingThreadExecutor;
import com.zwl.jsoup.thread.DownloadDocumentThread;
import com.zwl.jsoup.thread.ParserDocumentCallback;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 爬取知乎所有话题神评
 *
 * @author zhao_wei_long
 * @since 2021/6/23
 **/
@Slf4j
@Component
public class WebCrawler {

  @Autowired
  ApplicationEventPublisher applicationEventPublisher;

  @Qualifier("topicMapper")
  @Autowired
  TopicMapper topicMapper;

  @Qualifier("answerMapper")
  @Autowired
  AnswerMapper answerMapper;

  @Autowired
  ParserDocumentCallback passerDocumentCallback;

  private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36";
  private static final String TOPICLIST = "https://www.zhihu.com/node/TopicsPlazzaListV2";
  private static final String TOPICURL = "https://www.zhihu.com/topics";
  private static final String ANSWERURL = "https://www.zhihu.com/api/v4/topics/19860414/feeds/essence?limit=50&offset=%d";
  private static final String ANSWER_FEED = "https://www.zhihu.com/question/%d/answer/%d";

  private final ListeningExecutorService listeningExecutorService;

  private final OkHttpClient client;

  private String cookie;


  public WebCrawler() {
    ThreadFactory threadFactory = new ThreadFactoryBuilder()
        .setNameFormat("crawling-pool-")
        //未铺货异常处理
        .setUncaughtExceptionHandler(
            (t, e) -> log.error("未捕获异常：{}，线程名称：{}", e.getMessage(), t.getName()))
        .build();
    listeningExecutorService = MoreExecutors.
        listeningDecorator(new CrawlingThreadExecutor(30, 100,
            30, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(3000), threadFactory, new CallerRunsPolicy()));

    client = new OkHttpClient.Builder().
        connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .callTimeout(5, TimeUnit.SECONDS)
        .connectionPool(new ConnectionPool(30, 5, TimeUnit.SECONDS))
        .addInterceptor(chain -> {
          Request request = chain.request();
          Request newRequest = request.newBuilder()
              .header("user-agent", USER_AGENT)
//              .header("Cookie", cookie)
              .build();
          return chain.proceed(newRequest);
        })
        .build();
  }


  /**
   * 开始任务
   */
  public void start(String cookie) throws Exception {
    this.cookie = cookie;
    startCrawlingZhihu();
  }


  /**
   * 查询所有子话题
   *
   * @param parentId 父话题id
   */
  private ConcurrentLinkedQueue<Topic> queryTopicChildren(Integer parentId) {
    ConcurrentLinkedQueue<Topic> topics = new ConcurrentLinkedQueue<>();
    int offset = 0;
    for (; ; ) {
      log.info("查询父话题：【{}】下，offset=【{}】的子话题", parentId, offset);
      FormBody body = new FormBody.Builder()
          .add("method", "next")
          .add("params",
              "{\"topic_id\":" + parentId + ",\"offset\":" + offset + ",\"hash_id\":\"\"}")
          .build();
      Request request = new Builder().url(TOPICLIST).post(body).build();
      String json;
      try {
        ResponseBody responseBody = client.newCall(request).execute().body();
        if (responseBody == null) {
          break;
        }
        json = responseBody.string();
      } catch (IOException e) {
        return topics;
      }
      JSONObject jsonObject = JSON.parseObject(json);
      JSONArray msg = jsonObject.getJSONArray("msg");
      if (msg == null || msg.isEmpty()) {
        return topics;
      }
      ConcurrentLinkedQueue<Topic> collect = msg.parallelStream().map(o -> {
        Document document = Jsoup.parse(((String) o));
        Element element = document.select("a[target=_blank]").first();
        String comment = document.getElementsByTag("p").first().text();
        String href = element.attr("href");
        String tid = StrUtil.subAfter(href, "/topic/", true);
        String desc = element.select("strong").text();
        Topic topic = new Topic();
        topic.setTopicId(Integer.parseInt(tid));
        topic.setTopicName(desc);
        topic.setParentId(Convert.toLong(parentId));
        topic.setComment(comment);
        return topic;
      }).collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
      topics.addAll(collect);
      offset += 20;
    }
    return topics;
  }

  /**
   * 提柜解析所有话题
   *
   * @param topics 当前话题
   */
  private void parseTopic(ConcurrentLinkedQueue<Topic> topics) {
    topics.forEach(topic -> {
          listeningExecutorService.execute(() -> {
            applicationEventPublisher.publishEvent(new QueryAnswerEvent(topic));
            Topic one = topicMapper
                .findOne(topicMapper.query().where.topicId().eq(topic.getTopicId()).end());
            if (one == null) {
              topicMapper.insert(topic);
            }
          });

          try {
            ConcurrentLinkedQueue<Topic> topicChildren = listeningExecutorService
                .submit(() -> queryTopicChildren(topic.getTopicId())).get();
            if (!topicChildren.isEmpty()) {
              parseTopic(topicChildren);
            }
          } catch (InterruptedException | ExecutionException e) {
            log.error("查询子话题：【{}】失败，msg:{}", topic.getTopicName(), e.getMessage());
          }
        }
    );
  }

  /**
   * 获取根话题
   *
   * @return 跟话题集合
   * @throws IOException
   */
  private ConcurrentLinkedQueue<Topic> getRootTopic() throws IOException {
    log.info("》》》》》》》》》》》》》》》开始查询根话题《《《《《《《《《《《《《《《《《《");
    Document document = Jsoup.connect(TOPICURL)
        .userAgent(USER_AGENT)
        .get();
    Elements elements = document.select(".zm-topic-cat-item");
    ConcurrentLinkedQueue<Topic> queue = elements.stream()
        .map(element -> {
          Topic topic = new Topic();
          topic.setTopicId(Integer.parseInt(element.attr("data-id")));
          topic.setTopicName(element.child(0).text());
          return topic;
        })
        .collect(Collectors.toCollection(ConcurrentLinkedQueue::new));

    List<Topic> topics = queue.parallelStream().filter(topic -> {
      Topic one = topicMapper
          .findOne(topicMapper.query().where.topicId().eq(topic.getTopicId()).end());
      return one == null;
    }).collect(Collectors.toList());
    topicMapper.insertBatch(topics);
    return queue;
  }


  /**
   * 获取所有话题列表
   *
   * @return 话题集合
   * @throws Exception
   */
  private void startCrawlingZhihu() throws Exception {
    log.info(">>>>>>>>>>>>>>>>>>>>>>>>>开始获取所有根话题列表>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    List<Topic> topics = topicMapper
        .listEntity(topicMapper.query().select.end().where.parentId().eq(0).end());
    ConcurrentLinkedQueue<Topic> rootTopic;
    if (topics.isEmpty()) {
      rootTopic = getRootTopic();
    } else {
      rootTopic = new ConcurrentLinkedQueue<>(topics);
    }
    log.info(">>>>>>>>>>>>>>>>>>>>>>>>>开始解析所有跟话题下子话题>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    parseTopic(rootTopic);
  }


  /**
   * 获取所有answer答案
   *
   * @throws IOException
   */
  @EventListener(value = QueryAnswerEvent.class)
  @Async
  public void getAllAnswer(QueryAnswerEvent queryAnswerEvent) throws IOException {
    Topic topic = ((Topic) queryAnswerEvent.getSource());
    log.info(">>>>>>>>>>>>>>>start:事件:获取【{}】话题下所有高赞答案<<<<<<<<<<<<<<<<<<", topic.getTopicName());
    boolean flg = true;
    int offset = 0;
    while (flg) {
      Request get = new Builder().url(String.format(ANSWERURL, offset)).build();
      String json = Objects.requireNonNull(client.newCall(get)
          .execute().body())
          .string();

      JSONObject object = JSON.parseObject(json);
      JSONObject paging = object.getJSONObject("paging");
      JSONArray data = object.getJSONArray("data");
      if (paging == null || data == null || data.isEmpty()) {
        break;
      }

      boolean isNotEnd = !paging.getBoolean("is_end");
      boolean allMatch = data.parallelStream().allMatch(o1 -> {
        JSONObject d1 = (JSONObject) o1;
        Integer voteup_count1 = d1.getJSONObject("target").getInteger("voteup_count");
        return voteup_count1 > 10000;
      });

      flg = isNotEnd && allMatch;

      List<Answer> answers = data.parallelStream().filter(o -> {
        JSONObject d = (JSONObject) o;
        Integer voteup_count = d.getJSONObject("target").getInteger("voteup_count");
        return voteup_count > 10000;
      }).map(obj -> {
        JSONObject d = (JSONObject) obj;
        JSONObject target = d.getJSONObject("target");
        JSONObject question = target.getJSONObject("question");
        JSONObject author = target.getJSONObject("author");
        Integer qid = null;
        String title = null;
        Integer created_time = target.getInteger("created_time");
        Integer answerId = target.getInteger("id");
        if (question != null) {
          qid = question.getInteger("id");
          title = question.getString("title");
        }
        return new Answer()
            .setTopicId(topic.getTopicId())
            .setAnswerId(answerId)
            .setExcerpt(target.getString("excerpt"))
            .setVoteupCount(target.getInteger("voteup_count"))
            .setQuestionId(qid)
            .setQuestion(title)
            .setAuthorName(author.getString("name"))
            .setCreateDate(
                created_time == null ? null : Date.from(Instant.ofEpochSecond(created_time)))
            .setAnswerUrl(String.format(ANSWER_FEED, qid, answerId));

      }).collect(Collectors.toList());
      offset += 10;
      int batch = answerMapper.insertBatch(answers);
      log.info("批量插入问题回答：{}", batch);
      applicationEventPublisher.publishEvent(new AnswerEvent(answers));
    }
    log.info(">>>>>>>>>>>>>>>end:事件:获取【{}】话题下所有高赞答案<<<<<<<<<<<<<<<<<<", topic.getTopicName());
  }


  /**
   * 解析topic下高赞回答
   *
   * @param answerEvent
   */
  @EventListener(AnswerEvent.class)
  @Async
  public void parseTopic(AnswerEvent answerEvent) {
    log.info(">>>>>>>>>>>>>>>start:事件：下载高赞答案回答>>>>>>>>>>>>>>>>>");
    List<Answer> answers = (List<Answer>) answerEvent.getSource();
    answers.forEach(answer -> {
      ListenableFuture<ParseDTO> listenableFuture = listeningExecutorService
          .submit(new DownloadDocumentThread(client, answer));
      Futures
          .addCallback(listenableFuture, passerDocumentCallback, listeningExecutorService);
    });
    log.info(">>>>>>>>>>>>>>>end:事件：下载高赞答案回答>>>>>>>>>>>>>>>>>");
  }


  /**
   * 解析回答dom
   *
   * @param domParseEvent
   */
  @EventListener(DomParseEvent.class)
  @Async
  public void doDocumentParse(DomParseEvent domParseEvent) {
    log.info("===================start:doDocumentParse:解析document===================");
    ParseDTO parseDTO = (ParseDTO) domParseEvent.getSource();
    Document document = parseDTO.getDocument();
    Answer answer = parseDTO.getAnswer();
    if (document == null) {
      return;
    }
    Element element = document.select(".RichText.ztext.CopyrightRichText-richText").first();
    if (element == null) {
      return;
    }
    List<Node> childNodes = element.childNodes();
    StringBuilder buffer = new StringBuilder();
    buffer.append("# ").append(answer.getQuestion()).append("\n\n");
    for (Node node : childNodes) {
      Element ele = (Element) node;
      String nodeName = node.nodeName();
      if ("figure".equals(nodeName)) {
        Element img = ele.select("img").first();
        ele.replaceWith(img);
        buffer.append(img.toString());
      } else {
        buffer.append(ele);
      }
      buffer.append("\n\n");
    }

    Answer update = new Answer()
        .setId(answer.getId())
        .setContent(buffer.toString())
        .setIsGodReplies(buffer.length() < 1500);
    answerMapper.updateById(update);
    log.info("===================end:doDocumentParse:解析document===================");
  }
}
