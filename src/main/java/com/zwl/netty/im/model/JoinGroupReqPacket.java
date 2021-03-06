package com.zwl.netty.im.model;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 加入群聊请求
 * @author ZhaoWeiLong
 * @since 2021/8/19
 **/
@EqualsAndHashCode(callSuper = false)
@Data
public class JoinGroupReqPacket extends Packet {

  private String groupId;

  private List<String> userIds;

  @Override
  public Byte getCommand() {
    return Command.JOIN_GROUP_REQUEST.getCode().byteValue();
  }
}
