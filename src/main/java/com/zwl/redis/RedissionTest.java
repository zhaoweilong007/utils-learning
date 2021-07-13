package com.zwl.redis;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;

/**
 * redis分布式锁
 * @author ZhaoWeiLong
 * @since 2021/7/12
 */
@Slf4j
public class RedissionTest extends TestCase {

  private final RedissonClient redissonClient = Redisson.create();

  private int amount = 0;

  private final HashMap<String, Object> map = new HashMap<>();

  /**
   * 使用读写锁实现线程安全的map
   *
   * @param key
   * @param value
   * @return
   */
  private Object put(String key, Object value) {
    RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("map");
    RLock writeLock = readWriteLock.writeLock();
    writeLock.lock();
    Object obj = map.put(key, value);
    writeLock.unlock();
    return obj;
  }

  /**
   * 获取value
   *
   * @param key
   * @return
   */
  private Object get(String key) {
    RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("map");
    RLock readLock = readWriteLock.readLock();
    readLock.lock();
    Object val = map.get(key);
    readLock.unlock();
    return val;
  }

  public void test() throws InterruptedException {
    ExecutorService executorServic = Executors.newFixedThreadPool(100);
    for (int i = 0; i < 1000; i++) {
      int finalI = i;
      executorServic.execute(() -> spike(finalI));
    }
    TimeUnit.SECONDS.sleep(3);
    executorServic.shutdown();
    log.info("amount result:{}", amount);
    assertEquals(1000, amount);

    // todo 其他特性，支持信号量、倒计时器、原子类、集合等
    RMap<String, Object> concurMap = redissonClient.<String, Object>getMap("concurMap");
    RSemaphore semaphore = redissonClient.getSemaphore("semaphore");
    RCountDownLatch countDownLatch = redissonClient.getCountDownLatch("countDownLatch");
  }

  private void spike(Integer integer) {
    RLock lock = redissonClient.getLock("lock");
    // 默认获取锁过期时间为30秒
    lock.lock();
    try {
      // 获取锁执行业务代码，业务执行时间过长会自动给锁延长
      TimeUnit.SECONDS.sleep(1);
      amount++;
      log.info("当前线程{},value={}", Thread.currentThread().getName(), integer);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
  }
}
