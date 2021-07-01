package com.zwl.jsoup.thread;

import com.google.common.util.concurrent.FutureCallback;
import com.zwl.jsoup.model.DomParseEvent;
import com.zwl.jsoup.model.ParseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 解析dom回调
 *
 * @author zhao_wei_long
 * @since 2021/6/25
 **/
@Slf4j
@Component
public class ParserDocumentCallback implements FutureCallback<ParseDTO> {

  @Autowired
  ApplicationEventPublisher applicationEventPublisher;

  @Override
  public void onSuccess(ParseDTO parseDTO) {
    log.info("====================触发回调==================");
    applicationEventPublisher.publishEvent(new DomParseEvent(parseDTO));
  }

  @Override
  public void onFailure(Throwable throwable) {
    log.error("下载document error:{}", throwable.getMessage());
  }
}
