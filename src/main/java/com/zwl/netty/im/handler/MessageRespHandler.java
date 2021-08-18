package com.zwl.netty.im.handler;

import com.zwl.netty.im.model.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息响应处理器
 *
 * @author ZhaoWeiLong
 * @since 2021/8/17
 **/
@Slf4j
public class MessageRespHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext,
      MessageResponsePacket msg) {
    log.info("{}:{} -> {}", msg.getFromUserId(), msg.getFromUserName(), msg.getMessage());
  }
}
