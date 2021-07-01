package com.zwl.jsoup;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.zwl.jsoup.thread.CrawlingThreadExecutor;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
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
    CrawlingThreadExecutor executor = new CrawlingThreadExecutor(20, 50, 30,
        TimeUnit.SECONDS, new LinkedBlockingQueue<>(2000),
        new CallerRunsPolicy());
    ThreadFactory threadFactory = new ThreadFactoryBuilder()
        .setNameFormat("Listener-Event-Pool-")
        //未铺货异常处理
        .setUncaughtExceptionHandler(
            (t, e) -> log.error("事件监听器未捕获异常：{}，线程名称：{}", e.getMessage(), t.getName()))
        .build();
    executor.setThreadFactory(threadFactory);
    return executor;
  }


}
