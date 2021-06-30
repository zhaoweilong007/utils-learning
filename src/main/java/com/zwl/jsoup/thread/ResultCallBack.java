package com.zwl.jsoup.thread;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.FutureCallback;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * 结果回调
 *
 * @author zhao_wei_long
 * @since 2021/6/25
 **/
@Slf4j
public class ResultCallBack implements FutureCallback<Map<String, Object>> {


  @Override
  public void onSuccess(@Nullable Map<String, Object> map) {
    log.info("获取解析结果：\n{}", JSON.toJSONString(map, true));
  }

  @Override
  public void onFailure(Throwable throwable) {
    log.error("解析document error:{}", throwable.getMessage());
  }
}
