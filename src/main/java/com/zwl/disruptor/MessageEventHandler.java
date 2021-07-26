package com.zwl.disruptor;

import com.google.gson.Gson;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 事件处理器
 *
 * @author ZhaoWeiLong
 * @since 2021/7/23
 */
@Slf4j
public abstract class MessageEventHandler<T>
    implements EventHandler<MessageEvent<T>>, WorkHandler<MessageEvent<T>> {

  private final Gson gson = new Gson();

  public MessageEventHandler() {}

  @Override
  public void onEvent(MessageEvent<T> event, long sequence, boolean endOfBatch) throws Exception {
    log.info("事件处理器执行...");
    log.info(
        "线程名称：{}，message:{},sequence：{}，endOfBatch：{}",
        Thread.currentThread().getName(),
        gson.toJson(event.getMsg()),
        sequence,
        endOfBatch);
    this.onEvent(event);
  }

  @Override
  public void onEvent(MessageEvent<T> event) throws Exception {
    log.info("工作处理器执行...");
    this.consumer(event.getMsg());
  }

  public abstract void consumer(T msg);
}
