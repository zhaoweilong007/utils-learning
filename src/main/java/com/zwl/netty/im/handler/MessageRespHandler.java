package com.zwl.netty.im.handler;

import com.zwl.netty.im.model.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhaoWeiLong
 * @since 2021/8/17
 **/
@Slf4j
public class MessageRespHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext,
      MessageResponsePacket messageResponsePacket) throws Exception {
    log.info("{}-收到服务器消息:{}", new Date(), messageResponsePacket.getMessage());
  }
}
