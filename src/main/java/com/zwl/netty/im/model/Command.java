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
   * 登录请求
   */
  LOGIN_REQUEST(1, LoginRequestPacket.class),

  /**
   * 登录响应
   */
  LOGIN_RESPONSE(2, LoginRespPacket.class),

  /**
   * 消息请求
   */
  MESSAGE_REQUEST(3, MessageRequestPacket.class),

  /**
   * 消息响应
   */
  MESSAGE_RESPONSE(4, MessageResponsePacket.class);


  private final Integer code;
  private final Class<? extends Packet> clazz;
}
