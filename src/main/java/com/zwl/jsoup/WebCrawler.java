package com.zwl.jsoup;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.zwl.mapper.AnswerMapper;
import com.zwl.mapper.TopicMapper;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
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
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

  @Qualifier("topicMapper")
  @Autowired
  TopicMapper topicMapper;

  @Qualifier("answerMapper")
  @Autowired
  AnswerMapper answerMapper;

  private final BlockingQueue<DocumentParseThread> parseThreads = new LinkedBlockingQueue<>(100);
  private final ListeningExecutorService listeningExecutorService;
  private final AtomicInteger count = new AtomicInteger(0);
  private final List<Map<String, Object>> resultData = Lists.newCopyOnWriteArrayList();
  public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36";
  private static final String TOPICLIST = "https://www.zhihu.com/node/TopicsPlazzaListV2";
  private static final String TOPICURL = "https://www.zhihu.com/topics";
  private final OkHttpClient client;
  private String cookie;


  public WebCrawler() {
    int availableProcessors = Runtime.getRuntime().availableProcessors();
    ThreadFactory threadFactory = new ThreadFactoryBuilder()
        .setNameFormat("crawling-pool-")
        //未铺货异常处理
        .setUncaughtExceptionHandler(
            (t, e) -> log.error("未捕获异常：{}，线程名称：{}", e.getMessage(), t.getName()))
        .build();
    listeningExecutorService = MoreExecutors.
        listeningDecorator(new CrawlingThreadExecutor(availableProcessors, availableProcessors * 10,
            10, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(), threadFactory, new CallerRunsPolicy()));

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
              .build();
          return chain.proceed(newRequest);
        })
        .build();
  }


  /*查询所有子话题*/
  public void queryTopicChildren(Integer parentId, Integer offset,
      ConcurrentLinkedQueue<Topic> topics) {
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
        return;
      }
      JSONObject jsonObject = JSON.parseObject(json);
      JSONArray msg = jsonObject.getJSONArray("msg");
      if (msg == null || msg.isEmpty()) {
        return;
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
  }

  /*递归解析所有话筒*/
  public void parseTopic(ConcurrentLinkedQueue<Topic> topics, ConcurrentLinkedQueue<Topic> result) {
    topics.parallelStream().forEach(topic -> {
          listeningExecutorService.execute(() -> {
            result.add(topic);
            Topic one = topicMapper
                .findOne(topicMapper.query().where.topicId().eq(topic.getTopicId()).end());
            if (one == null) {
              topicMapper.insert(topic);
            }
          });
          ConcurrentLinkedQueue<Topic> linkedQueue = new ConcurrentLinkedQueue<>();
          log.info("查询话题：{}", topic.getTopicName());
          queryTopicChildren(topic.getTopicId(), 0, linkedQueue);
          if (!linkedQueue.isEmpty()) {
            parseTopic(linkedQueue, result);
          }
/*      ListenableFuture<ConcurrentLinkedQueue<Topic>> listenableFuture = listeningExecutorService
          .submit(() -> {
            ConcurrentLinkedQueue<Topic> linkedQueue = new ConcurrentLinkedQueue<>();
            log.info("查询话题：{}", topic.getTopicName());
            queryTopicChildren(topic.getTopicId(), 0, linkedQueue);
            return linkedQueue;
          });
      Futures.addCallback(listenableFuture, new FutureCallback<ConcurrentLinkedQueue<Topic>>() {
        @Override
        public void onSuccess(@Nullable ConcurrentLinkedQueue<Topic> topics) {
          if (topics != null && !topics.isEmpty()) {
            try {
              semaphore.acquire();
              parseTopic(topics, result);
            } catch (Exception e) {
              e.printStackTrace();
            } finally {
              semaphore.release();
            }
          }
        }
        @Override
        public void onFailure(Throwable throwable) {
        }
      }, listeningExecutorService);*/
        }
    );

  }

  public ConcurrentLinkedQueue<Topic> getTopicList() throws Exception {
    log.info(">>>>>>>>>>>>>>>>>>>>>>>>>开始获取所有话题列表>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
/*    Document document = Jsoup.connect(TOPICURL)
        .userAgent(USER_AGENT)
        .get();
    Elements elements = document.select(".zm-topic-cat-item");
    ConcurrentLinkedQueue<Topic> topics = elements.stream()
        .map(element -> {
          Topic topic = new Topic();
          topic.setTopicId(Integer.parseInt(element.attr("data-id")));
          topic.setTopicName(element.child(0).text());
          return topic;
        })
        .collect(Collectors.toCollection(ConcurrentLinkedQueue::new));*/
    List<Topic> topics = topicMapper
        .listEntity(topicMapper.query().select.end().where.parentId().eq(0).end());
    ConcurrentLinkedQueue<Topic> linkedQueue = new ConcurrentLinkedQueue<>(topics);

    ConcurrentLinkedQueue<Topic> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
    parseTopic(linkedQueue, concurrentLinkedQueue);
    return concurrentLinkedQueue;
  }

  public static final String ANSWERURL = "https://www.zhihu.com/api/v4/topics/19860414/feeds/essence?limit=50&offset=%d";

  public static final String ANSWER_FEED = "https://www.zhihu.com/question/%d/answer/%d";

  /**
   * 开始任务
   */
  public void start(String cookie) throws Exception {
    this.cookie = cookie;
//    ConcurrentLinkedQueue<Topic> topics = getTopicList();
    List<Topic> list = topicMapper.listEntity(topicMapper.query().where.parentId().eq(-1).end());
    ConcurrentLinkedQueue<Topic> topics = new ConcurrentLinkedQueue<>(list);
    while (!topics.isEmpty()) {
      Topic topic = topics.poll();
      boolean flg = true;
      int offset = 0;
      while (flg) {
        Request get = new Builder().url(String.format(ANSWERURL, offset)).build();
        String json = client.newCall(get)
            .execute().body()
            .string();

        JSONObject object = JSON.parseObject(json);
        JSONObject paging = object.getJSONObject("paging");
        JSONArray data = object.getJSONArray("data");

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
              .setCreateDate(created_time==null?null:Date.from(Instant.ofEpochSecond(created_time)))
              .setAnswerUrl(String.format(ANSWER_FEED, qid, answerId));

        }).collect(Collectors.toList());

        offset += 10;

        int batch = answerMapper.insertBatch(answers);
        log.info("批量插入问题回答：{}", batch);
      }
    }

    log.info("》》》》》》》》》》》》开始爬虫网页《《《《《《《《《《《《《《");
    ParserDocumentCallback passerDocumentCallback = new ParserDocumentCallback(parseThreads, count);
    ResultCallBack resultCallBack = new ResultCallBack(resultData);
    Elements elements = Jsoup.parse("").select("");

    elements.parallelStream().forEach(
        element -> {
          DownloadDocumentThread downloadDocumentThread = new DownloadDocumentThread(client,
              element.select("").text());
          ListenableFuture<Document> listenableFuture = listeningExecutorService
              .submit(downloadDocumentThread);
          count.incrementAndGet();
          Futures
              .addCallback(listenableFuture, passerDocumentCallback, listeningExecutorService);
        });

    while (count.get() != 0) {
      DocumentParseThread parseThread;
      try {
        parseThread = parseThreads.take();
        ListenableFuture<Map<String, Object>> listenableFuture = listeningExecutorService
            .submit(parseThread);
        Futures.addCallback(listenableFuture, resultCallBack, listeningExecutorService);
      } catch (InterruptedException e) {
        log.error("队列InterruptedException:{}", e.getMessage());
      }
    }

    log.info("》》》》》》》》》》》》》》》》》网页爬虫结束《《《《《《《《《《《《《《《《《《《");
    resultData.parallelStream().forEach(System.out::println);

  }


}
