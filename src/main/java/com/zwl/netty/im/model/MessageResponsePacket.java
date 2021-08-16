package com.zwl.netty.im.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ZhaoWeiLong
 * @since 2021/8/16
 **/
@EqualsAndHashCode(callSuper = false)
@Data
public class MessageResponsePacket extends Packet {

  private String message;

  @Override
  public Byte getCommand() {
    return Command.MESSAGE_RESPONSE.getCode().byteValue();
  }
}
