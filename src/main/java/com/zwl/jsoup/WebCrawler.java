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
import com.zwl.mapper.TopicMapper;
import java.io.IOException;
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
        listeningDecorator(new CrawlingThreadExecutor(availableProcessors, availableProcessors * 5,
            10, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(), threadFactory, new CallerRunsPolicy()));

    client = new OkHttpClient.Builder().
        connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .callTimeout(5, TimeUnit.SECONDS)
//        .connectionPool(new ConnectionPool(15, 5, TimeUnit.SECONDS))
        .addInterceptor(chain -> {
          Request request = chain.request();
          Request newRequest = request.newBuilder()
              .header("user-agent", USER_AGENT)
              .build();
          return chain.proceed(newRequest);
        })
        .build();
  }


  /*递归查询所有子话题*/
  public void queryTopicChildren(Integer parentId, Integer offset,
      ConcurrentLinkedQueue<Topic> topics) {
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
        return;
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
    queryTopicChildren(parentId, offset + 20, topics);
  }

  /*递归解析所有话筒*/
  public void parseTopic(ConcurrentLinkedQueue<Topic> topics, ConcurrentLinkedQueue<Topic> result) {
    topics.parallelStream().forEach(topic -> {
      result.add(topic);
      Topic one = topicMapper
          .findOne(topicMapper.query().where.topicId().eq(topic.getTopicId()).end());
      if (one == null) {
        topicMapper.insert(topic);
      }
      ConcurrentLinkedQueue<Topic> linkedQueue = new ConcurrentLinkedQueue<>();
      log.info("查询话题：{}", topic.getTopicName());
      queryTopicChildren(topic.getTopicId(), 0, linkedQueue);
      if (!linkedQueue.isEmpty()) {
        parseTopic(linkedQueue, result);
      }
    });
  }

  public ConcurrentLinkedQueue<Topic> getTopicList() throws IOException {
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


  /**
   * 开始任务
   */
  public void start(String cookie) throws IOException, InterruptedException {
    this.cookie = cookie;
    ConcurrentLinkedQueue<Topic> topicList = getTopicList();
    listeningExecutorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
    log.info(">>>>>>>>>>>>>>>>>话题处理完成>>>>>>>>>>>>>>>>>>>>>>>>>>>");

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
