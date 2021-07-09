package com.zwl.jsoup;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.zwl.jsoup.thread.CrawlingThreadExecutor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ZhaoWeiLong
 * @since 2021/7/9
 **/
@Configuration(proxyBeanMethods = false)
@Slf4j
public class WebCrawlerConfig {

  private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36";

  @Value("${zhihu.cookie}")
  private String cookie;

  @Bean
  public OkHttpClient client() {
    return new OkHttpClient.Builder().
        connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .callTimeout(10, TimeUnit.SECONDS)
        .connectionPool(new ConnectionPool(100, 5, TimeUnit.SECONDS))
        .addInterceptor(chain -> {
          Request request = chain.request();
          Request newRequest = request.newBuilder()
              .header("user-agent", USER_AGENT)
              .header("cookie",cookie)
              .build();
          return chain.proceed(newRequest);
        })
        .build();
  }

  @Bean
  public ListeningExecutorService listeningExecutorService() {
    ThreadFactory threadFactory = new ThreadFactoryBuilder()
        .setNameFormat("crawling-pool-%d")
        //未铺货异常处理
        .setUncaughtExceptionHandler(
            (t, e) -> log.error("未捕获异常：{}，线程名称：{}", e.getMessage(), t.getName()))
        .build();
    return MoreExecutors.
        listeningDecorator(new CrawlingThreadExecutor(100, 200,
            30, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(), threadFactory, new CallerRunsPolicy()));
  }

}
