package com.zwl.jsoup;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.zwl.jsoup.thread.CrawlingThreadExecutor;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

/**
 * @author zhao_wei_long
 * @since 2021/7/1
 **/
@Configuration(proxyBeanMethods = false)
@Slf4j
public class AsyncConfig implements AsyncConfigurer {

  @Override
  public Executor getAsyncExecutor() {
    CrawlingThreadExecutor executor = new CrawlingThreadExecutor(50, 100, 30,
        TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
        new CallerRunsPolicy());
    ThreadFactory threadFactory = new ThreadFactoryBuilder()
        .setNameFormat("Listener-Event-Pool-%d")
        //未铺货异常处理
        .setUncaughtExceptionHandler(
            (t, e) -> log.error("事件监听器未捕获异常：{}，线程名称：{}", e.getMessage(), t.getName()))
        .build();
    executor.setThreadFactory(threadFactory);
    return executor;
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return new SimpleAsyncUncaughtExceptionHandler();
  }
}
