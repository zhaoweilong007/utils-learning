package com.zwl.netty.im.handler;

import com.zwl.netty.im.model.LoginOutRequestPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 登出处理器
 * @author ZhaoWeiLong
 * @since 2021/8/19
 **/
@Slf4j
public class LoginOutRequestHandler extends SimpleChannelInboundHandler<LoginOutRequestPacket> {

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext,
      LoginOutRequestPacket loginOutRequestPacket) throws Exception {

  }
}
