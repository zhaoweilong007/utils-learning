package com.zwl.jsoup.model;

import org.springframework.context.ApplicationEvent;

/**
 * @author zhao_wei_long
 * @since 2021/7/1
 **/
public class DomParseEvent extends ApplicationEvent {

  public DomParseEvent(ParseDTO parseDTO) {
    super(parseDTO);
  }
}
