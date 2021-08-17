package com.zwl.netty.im.utils;

import com.zwl.netty.im.model.Attributes;
import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * 登录工具类
 *
 * @author ZhaoWeiLong
 * @since 2021/8/16
 **/
@Slf4j
public class LogUtils {

  /**
   * 是否已登录
   *
   * @param channel
   * @return
   */
  public static Boolean hasLogin(Channel channel) {
    Attribute<Boolean> attr = channel.attr(Attributes.LOGIN);
    return attr != null && attr.get() != null && attr.get();
  }

  /**
   * 标记为登录
   *
   * @param channel
   */
  public static void markAsLogin(Channel channel) {
    Boolean flg = channel.attr(Attributes.LOGIN).getAndSet(true);
    log.info("设置登录状态：{}",flg);
  }

}
