package com.zwl.jsoup.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;

/**
 * 线程池
 * @author zhao_wei_long
 * @since 2021/6/25
 **/
@Slf4j
public class CrawlingThreadExecutor extends ThreadPoolExecutor {

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
