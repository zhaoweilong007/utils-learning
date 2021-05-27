package com.zwl.guava.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author ZhaoWeiLong
 * @date 2021/5/21
 **/
@Slf4j
public class CacheTest {

  @Test
  public void cacheTest() {
    /**
     * todo cache 线程安全的缓存，与concurrentHashMap类似，有三种回收方式，基于容量的回收，定时回收，基于引用回收
     */
    LoadingCache<Integer, String> cache
        = CacheBuilder.newBuilder()
        //并发级别，同时写缓存的线程数，并发数设置为cpu的核心数
        .concurrencyLevel(Runtime.getRuntime().availableProcessors())
        //初始容量
        .initialCapacity(10)
        //最大容量，超出后按照LRU 最近最少使用算法清除缓存
        .maximumSize(100)
        //权重
//        .maximumWeight()
        //缓存使用情况
        .recordStats()
        //设置写缓存后多久过期
        .expireAfterWrite(10, TimeUnit.SECONDS)
        //设置读缓存后多久过期
        .expireAfterAccess(10, TimeUnit.SECONDS)
        //设置缓存移除监听，同步监听会影响缓存请求
        .removalListener(notification -> {
          log.info("缓存key:{},value:{}被移除，移除原因:{}", notification.getKey(), notification.getValue(),
              notification.getCause());
        })
        //或者使用异步的监听
        /*    .removalListener(RemovalListeners.asynchronous(removalNotification -> {
              log.info("缓存key:{},value:{}被移除，移除原因:{}", removalNotification.getKey(), removalNotification.getValue(),
                  removalNotification.getCause());
            }, Runnable::run))*/
        //build方法可指定cacheLoader，在缓存不存在时通过 cacheLoader实现自动加载缓存
        .build(new DemoCacheLoader());

    //测试并发情况下获取缓存
    LinkedBlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>();
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 6, 5, TimeUnit.SECONDS,
        blockingQueue);

    blockingQueue.add(() -> {
      for (int i = 0; i < 20; i++) {
        try {
          String val = cache.get(i);
          log.info("线程【{}】,获取第【{}】,值【{}】", Thread.currentThread().getName(), i, val);
          TimeUnit.SECONDS.sleep(1);
        } catch (ExecutionException | InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    blockingQueue.add(() -> {
      for (int i = 0; i < 20; i++) {
        try {
          String val = cache.get(i);
          log.info("线程【{}】,获取第【{}】,值【{}】", Thread.currentThread().getName(), i, val);
          TimeUnit.SECONDS.sleep(3);
        } catch (ExecutionException | InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    BlockingQueue<Runnable> queue = threadPoolExecutor.getQueue();

    queue.forEach(threadPoolExecutor::execute);

    while (!threadPoolExecutor.isTerminated()) {

    }
    threadPoolExecutor.shutdown();
    log.info("统计缓存情况：{}", cache.stats());

    new Thread(() -> {

      //使用第二个参数指定key不存在时返回结果
      try {
        String val1 = cache.get(11, () -> {
          log.info("{}-加载val1的数据", Thread.currentThread().getName());
          TimeUnit.SECONDS.sleep(1);
          return "val1";
        });
        log.info("加载val1的数据完成：{}", val1);
      } catch (ExecutionException e) {
        e.printStackTrace();
      }

    }).start();

    //当两个线程同时获取一个key时，cache只会启用一个线程加载callable的方法
    new Thread(() -> {
      try {
        String val2 = cache.get(11, () -> {
          log.info("{}-加载val2的数据", Thread.currentThread().getName());
          TimeUnit.SECONDS.sleep(1);
          return "val2";
        });
        log.info("加载val1的数据完成：{}", val2);
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    }).start();


  }


  /**
   * 缓存加载策略  key不存在时自动加载
   */
  @Slf4j
  static class DemoCacheLoader extends CacheLoader<Integer, String> {

    @Override
    public String load(Integer s) throws Exception {
      log.info("加载缓存开始：{}", Thread.currentThread().getName());
      TimeUnit.SECONDS.sleep(4);
      Random random = new Random();
      log.info("加载缓存结束：{}", Thread.currentThread().getName());
      return "value:" + random.nextInt(1000);
    }
  }

}
