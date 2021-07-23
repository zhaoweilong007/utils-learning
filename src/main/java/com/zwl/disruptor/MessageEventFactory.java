package com.zwl.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * 事件工厂
 * @author ZhaoWeiLong
 * @since 2021/7/23
 */
public class MessageEventFactory<T> implements EventFactory<MessageEvent<T>> {

  @Override
  public MessageEvent<T> newInstance() {
    return new MessageEvent<>();
  }
}
