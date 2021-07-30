package com.zwl.netty.im.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 指令
 *
 * @author ZhaoWeiLong
 * @since 2021/7/27
 **/
@Getter
@AllArgsConstructor
public enum Command {

  /**
   * 请求
   */
  LOGIN_REQUEST(1, LoginRequestPacket.class),

  /**
   * 请求响应
   */
  LOGIN_RESPONSE(2, LoginRespPacket.class);


  private final Integer code;
  private final Class<? extends Packet> clazz;
}
