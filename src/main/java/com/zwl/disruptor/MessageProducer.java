package com.zwl.disruptor;

import com.lmax.disruptor.RingBuffer;

/**
 * 发布者
 *
 * @author ZhaoWeiLong
 * @since 2021/7/23
 **/
public class MessageProducer {

  private final RingBuffer<MessageEvent<String>> ringBuffer;

  public MessageProducer(RingBuffer<MessageEvent<String>> ringBuffer) {
    this.ringBuffer = ringBuffer;
  }

  public void publishEvent(String msg) {
    //获取下标，获取不到将一直阻塞
    long sequence = ringBuffer.next();
    try {
      //填充数据
      MessageEvent<String> event = ringBuffer.get(sequence);
      event.setMsg(msg);
    } finally {
      //最后发布，将sequence传递给消费者
      ringBuffer.publish(sequence);
    }

  }

}
