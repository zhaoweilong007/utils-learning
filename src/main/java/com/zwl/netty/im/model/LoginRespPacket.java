package com.zwl.netty.im.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ZhaoWeiLong
 * @since 2021/7/30
 **/
@EqualsAndHashCode(callSuper = false)
@Data
public class LoginRespPacket extends Packet {

  private String msg;
  private Boolean success;
  private String token;

  @Override
  public Byte getCommand() {
    return Command.LOGIN_RESPONSE.getCode().byteValue();
  }
}
