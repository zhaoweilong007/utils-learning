package com.zwl.disruptor;

import com.google.gson.Gson;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 事件处理器
 * @author ZhaoWeiLong
 * @since 2021/7/23
 **/
@Slf4j
public class MessageEventHandler<T> implements EventHandler<MessageEvent<T>> {

  private final Gson gson = new Gson();

  @Override
  public void onEvent(MessageEvent<T> event, long sequence, boolean endOfBatch) throws Exception {
    log.info("事件处理器执行...");
    log.info("线程名称：{}，message:{},sequence：{}，endOfBatch：{}",
        Thread.currentThread().getName(), gson.toJson(event.getMsg()), sequence, endOfBatch);
  }
}
