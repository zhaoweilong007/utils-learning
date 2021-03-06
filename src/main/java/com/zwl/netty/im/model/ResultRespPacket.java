package com.zwl.netty.im.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 统一响应
 * @author ZhaoWeiLong
 * @since 2021/8/20
 **/
@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultRespPacket<T> extends Packet {

  private Boolean success;

  private String msg;

  private T data;

  @Override
  public Byte getCommand() {
    return Command.RESULT_RESPONSE.getCode().byteValue();
  }


}
