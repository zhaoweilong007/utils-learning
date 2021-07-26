package com.zwl.disruptor;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhaoWeiLong
 * @since 2021/7/26
 */
@Slf4j
public class StringMsgConsumer extends MessageEventHandler<String> {

  @Override
  public void consumer(String msg) {
    log.info("消费数据：{}", msg);
  }
}
