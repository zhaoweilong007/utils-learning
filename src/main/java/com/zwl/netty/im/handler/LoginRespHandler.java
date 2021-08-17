package com.zwl.netty.im.handler;

import com.zwl.netty.im.model.LoginRespPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhaoWeiLong
 * @since 2021/8/17
 **/
@Slf4j
public class LoginRespHandler extends SimpleChannelInboundHandler<LoginRespPacket> {


  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext,
      LoginRespPacket loginRespPacket) throws Exception {
    log.info(loginRespPacket.getMsg());
  }
}
