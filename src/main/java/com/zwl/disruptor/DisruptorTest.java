package com.zwl.disruptor;

import cn.hutool.core.util.RandomUtil;
import com.google.common.base.Stopwatch;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhaoWeiLong
 * @since 2021/7/23
 */
@Slf4j
public class DisruptorTest {

  public static void main(String[] args) {
    // 线程池
    ExecutorService executorService = Executors.newCachedThreadPool();
    // 事件工厂
    MessageEventFactory<String> factory = new MessageEventFactory<>();
    // buffSize
    int buffSize = 1024 * 1024;
    // 构造 支持一个发布者 ProducerType.SINGLE
    Disruptor<MessageEvent<String>> disruptor =
        new Disruptor<>(
            factory, buffSize, executorService, ProducerType.SINGLE, new SleepingWaitStrategy());
    disruptor.handleEventsWithWorkerPool(new StringMsgConsumer());
    Stopwatch stopwatch = Stopwatch.createStarted();
    disruptor.start();

    RingBuffer<MessageEvent<String>> ringBuffer = disruptor.getRingBuffer();
    MessageProducer producer = new MessageProducer(ringBuffer);
    for (int i = 0; i < 10000; i++) {
      producer.publishEvent(RandomUtil.randomString(8) + "-" + i);
    }
    disruptor.shutdown();
    executorService.shutdown();
    stopwatch.stop();
    long elapsed = stopwatch.elapsed(TimeUnit.SECONDS);
    log.info("总耗时：{}s", elapsed);
  }
}
