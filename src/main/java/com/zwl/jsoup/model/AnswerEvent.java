package com.zwl.jsoup.model;

import java.util.List;
import org.springframework.context.ApplicationEvent;

/**
 * @author zhao_wei_long
 * @since 2021/6/30
 **/
public class AnswerEvent extends ApplicationEvent {

  public AnswerEvent(List<Answer> answers) {
    super(answers);
  }
}
