package com.zwl.netty.im.handler;

import com.zwl.netty.im.model.MessageRequestPacket;
import com.zwl.netty.im.model.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhaoWeiLong
 * @since 2021/8/17
 **/
@Slf4j
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {

  @Override
  protected void channelRead0(ChannelHandlerContext ctx,
      MessageRequestPacket messageRequestPacket) throws Exception {
    String message = messageRequestPacket.getMessage();
    MessageResponsePacket responsePacket = new MessageResponsePacket();
    responsePacket.setMessage(message);
    ctx.writeAndFlush(responsePacket);
  }
}
