package com.zwl.jsoup;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.ResponseBody;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * @author zhao_wei_long
 * @since 2021/6/23
 **/
@Slf4j
public class WebCrawling {

  private final BlockingQueue<DocumentParseThread> parseThreads;
  private final ListeningExecutorService listeningExecutorService;
  private static final String rootUrl = "";

  private final OkHttpClient client = new OkHttpClient.Builder().
      connectTimeout(5, TimeUnit.SECONDS)
      .readTimeout(5, TimeUnit.SECONDS)
      .writeTimeout(5, TimeUnit.SECONDS)
      .callTimeout(5, TimeUnit.SECONDS)
      .connectionPool(new ConnectionPool(10, 5, TimeUnit.SECONDS))
      .build();

  /**
   * @param parseThreads 解析队列
   */
  public WebCrawling(BlockingQueue<DocumentParseThread> parseThreads) {
    this.parseThreads = parseThreads;
    listeningExecutorService = MoreExecutors.
        listeningDecorator(
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4));
  }


  /**
   * 开始任务
   *
   * @throws IOException
   */
  public void start() throws IOException {
    Optional<ResponseBody> body = Optional
        .ofNullable(client.newCall(new Builder().url(rootUrl).get().build()).execute().body());
    body.ifPresent(responseBody -> {
      log.info("》》》》》》》》》》》》开始爬虫网页《《《《《《《《《《《《《《");
      Document document = null;
      try {
        document = Jsoup.parse(responseBody.string());
        Elements elements = document.select("");
        LinkedList<ListenableFuture<Document>> futures = Lists
            .<ListenableFuture<Document>>newLinkedList();
        elements.parallelStream().forEach(
            element -> {
              DownloadDocumentThread downloadDocumentThread = new DownloadDocumentThread(client,
                  element.select("").text());
              ListenableFuture<Document> listenableFuture = listeningExecutorService
                  .submit(downloadDocumentThread);
              Futures.addCallback(listenableFuture, new FutureCallback<Document>() {
                @Override
                public void onSuccess(@Nullable Document document) {
                  try {
                    parseThreads.put(new DocumentParseThread(document));
                  } catch (InterruptedException e) {
                    log.error("InterruptedException:{}", e.getMessage());
                    Thread.currentThread().interrupt();
                  }
                }

                @Override
                public void onFailure(Throwable throwable) {
                  log.error("下载document error:{}", throwable.getMessage());
                }
              }, listeningExecutorService);
            });

        while (true) {
          DocumentParseThread parseThread = parseThreads.take();
          ListenableFuture<Map<String, Object>> listenableFuture = listeningExecutorService
              .submit(parseThread);
          Futures.addCallback(listenableFuture, new FutureCallback<Map<String, Object>>() {
            @Override
            public void onSuccess(@Nullable Map<String, Object> map) {
              log.info("获取解析结果：\n{}", JSON.toJSONString(map, true));
            }

            @Override
            public void onFailure(Throwable throwable) {
              log.error("解析document error:{}", throwable.getMessage());
            }
          }, listeningExecutorService);
        }


      } catch (IOException | InterruptedException e) {
        log.error("》》》》》》》》》》》解析root document失败：{}", e.getMessage());
      }
    });
  }


  /**
   * 停止任务，正在执行的任务将在执行完后停止
   */
  public void stop() {

  }

}
