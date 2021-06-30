package com.zwl.jsoup.model;

import org.springframework.context.ApplicationEvent;

/**
 * @author zhao_wei_long
 * @since 2021/6/30
 **/
public class QueryAnswerEvent extends ApplicationEvent {

  public QueryAnswerEvent(Topic topic) {
    super(topic);
  }
}
