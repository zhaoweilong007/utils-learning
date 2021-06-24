package com.zwl.jsoup;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * 爬取知乎所有话题神评
 *
 * @author zhao_wei_long
 * @since 2021/6/23
 **/
@Slf4j
public class WebCrawling {

  private final BlockingQueue<DocumentParseThread> parseThreads;
  private final ListeningExecutorService listeningExecutorService;
  private static final String topicUrl = "https://www.zhihu.com/topics";
  private final AtomicInteger count = new AtomicInteger();
  private final List<Map<String, Object>> resultData = Lists.newCopyOnWriteArrayList();
  public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36";
  private final OkHttpClient client;
  private String cookie;


  /**
   * 自定义线程池实现统计
   */
  private static class CrawlingThreadExecutor extends ThreadPoolExecutor {

    private final AtomicInteger alive = new AtomicInteger();
    private final AtomicLong totalTime = new AtomicLong();
    private final ThreadLocal<Long> startTIme = new ThreadLocal<>();

    public CrawlingThreadExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
        TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
        RejectedExecutionHandler handler) {
      super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
      startTIme.set(System.currentTimeMillis());
      alive.incrementAndGet();
      super.beforeExecute(t, r);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
      super.afterExecute(r, t);
      totalTime.addAndGet(System.currentTimeMillis() - startTIme.get());
      startTIme.remove();
    }

    @Override
    protected void terminated() {
      log.info("全部执行完成，总耗时：{}ms，平均耗时：{}ms",
          totalTime,
          totalTime.get() / alive.get());
      super.terminated();
    }
  }


  /**
   * @param parseThreads 解析队列
   */
  public WebCrawling(BlockingQueue<DocumentParseThread> parseThreads) {
    int availableProcessors = Runtime.getRuntime().availableProcessors();
    this.parseThreads = parseThreads;
    ThreadFactory threadFactory = new ThreadFactoryBuilder()
        .setNameFormat("crawling-pool-")
        //未铺货异常处理
        .setUncaughtExceptionHandler(
            (t, e) -> log.error("未捕获异常：{}，线程名称：{}", e.getMessage(), t.getName()))
        .build();
    listeningExecutorService = MoreExecutors.
        listeningDecorator(new CrawlingThreadExecutor(availableProcessors, availableProcessors * 4,
            5, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(), threadFactory, new CallerRunsPolicy()));

    client = new OkHttpClient.Builder().
        connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .callTimeout(5, TimeUnit.SECONDS)
        .connectionPool(new ConnectionPool(10, 5, TimeUnit.SECONDS))
        .addInterceptor(chain -> {
          Request request = chain.request();
          Request newRequest = request.newBuilder().header("user-agent", USER_AGENT)
              .header("cookie", cookie).build();
          return chain.proceed(newRequest);
        })
        .build();
  }

  class ParserDocumentCallback implements FutureCallback<Document> {

    @Override
    public void onSuccess(@Nullable Document document) {
      try {
        parseThreads.put(new DocumentParseThread(document));
        count.decrementAndGet();
      } catch (InterruptedException e) {
        log.error("InterruptedException:{}", e.getMessage());
        Thread.currentThread().interrupt();
      }
    }

    @Override
    public void onFailure(Throwable throwable) {
      log.error("下载document error:{}", throwable.getMessage());
    }
  }

  class ResultCallBack implements FutureCallback<Map<String, Object>> {

    @Override
    public void onSuccess(@Nullable Map<String, Object> map) {
      log.info("获取解析结果：\n{}", JSON.toJSONString(map, true));
      resultData.add(map);
    }

    @Override
    public void onFailure(Throwable throwable) {
      log.error("解析document error:{}", throwable.getMessage());
    }
  }

  /**
   * 开始任务
   */
  public void start(String cookie) throws IOException {
    this.cookie = cookie;
    Document root;
    try {
      //解析话题
      root = Jsoup.connect(topicUrl)
          .userAgent(USER_AGENT)
//          .cookie("cookie", cookie)
          .get();

    } catch (IOException e) {
      log.error("爬取话题列表：{}", e.getMessage());
      return;
    }

    log.info("》》》》》》》》》》》》开始爬虫网页《《《《《《《《《《《《《《");
    ParserDocumentCallback passerDocumentCallback = new ParserDocumentCallback();
    ResultCallBack resultCallBack = new ResultCallBack();
    Elements elements = root.select("");

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
