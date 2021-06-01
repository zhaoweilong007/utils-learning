package com.zwl.guava.eventbus;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 事件总线 发布-订阅模式 Test
 *
 * @author ZhaoWeiLong
 * @date 2021/6/1
 **/
public class EventBusTest {

  @Data
  static class Message {

    private String content;
  }

  @Slf4j
  static class MessageListener {

    @Subscribe
    public void handlerMessage(Message message) throws InterruptedException {
      log.info("线程名称：{},message:{}", Thread.currentThread().getName(), message.getContent());
      TimeUnit.SECONDS.sleep(1);
    }
  }


  @Test
  public void test() throws InterruptedException {
    // todo 一个简单的发布-订阅
    EventBus eventBus = new EventBus("test");
    eventBus.register(new MessageListener());
    Message message = new Message();
    message.setContent("测试消息");
    eventBus.post(message);

    //异步消息处理
    AsyncEventBus asyncEventBus = new AsyncEventBus("async", Executors.newFixedThreadPool(5));
    MessageListener listener = new MessageListener();
    asyncEventBus.register(listener);
    for (int i = 0; i < 10; i++) {
      Message msg = new Message();
      msg.setContent("测试消息" + i);
      asyncEventBus.post(msg);
    }
    TimeUnit.SECONDS.sleep(11);

  }

}
