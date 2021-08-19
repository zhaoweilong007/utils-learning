package com.zwl.netty.im.handler;

import com.zwl.netty.im.model.CreateGroupRespPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 创建群聊处理器
 *
 * @author ZhaoWeiLong
 * @since 2021/8/19
 **/
@Slf4j
public class CreateGroupRespHandler extends
    SimpleChannelInboundHandler<CreateGroupRespPacket> {

  @Override
  protected void channelRead0(ChannelHandlerContext ctx,
      CreateGroupRespPacket createGroupRespPacket) throws Exception {
    log.info("创建群聊成功，groupId：{},群成员：{}", createGroupRespPacket.getGroupId(),
        createGroupRespPacket.getUserNames().toArray());
  }
}
