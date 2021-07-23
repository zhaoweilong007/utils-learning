package com.zwl.disruptor;

import com.lmax.disruptor.RingBuffer;
import lombok.AllArgsConstructor;

/**
 * 发布者
 * @author ZhaoWeiLong
 * @since 2021/7/23
 **/
@AllArgsConstructor
public class MessageProducer<T> {

  private final RingBuffer<MessageEvent<T>> ringBuffer;

  public void publishEvent(T msg) {
    //获取下标，获取不到将一直阻塞
    long sequence = ringBuffer.next();
    try {
      //填充数据
      MessageEvent<T> event = ringBuffer.get(sequence);
      event.setMsg(msg);
    } finally {
      //最后发布，将sequence传递给消费者
      ringBuffer.publish(sequence);
    }

  }

}
