package com.zwl.netty.im.handler;

import com.zwl.netty.im.model.JoinGroupReqPacket;
import com.zwl.netty.im.model.JoinGroupRespPacket;
import com.zwl.netty.im.utils.LogUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.DefaultChannelGroup;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhaoWeiLong
 * @since 2021/8/19
 **/
@Slf4j
public class JoinGroupReqHandler extends SimpleChannelInboundHandler<JoinGroupReqPacket> {

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext,
      JoinGroupReqPacket joinGroupReqPacket) throws Exception {
    String groupId = joinGroupReqPacket.getGroupId();
    DefaultChannelGroup groupMap = LogUtils.getGroupMap(groupId);

    JoinGroupRespPacket respPacket = new JoinGroupRespPacket();

    if (groupMap != null) {
      groupMap.add(channelHandlerContext.channel());
      respPacket.setSuccess(true);
    } else {
      respPacket.setSuccess(false);
    }
    channelHandlerContext.channel().writeAndFlush(respPacket);
  }
}
