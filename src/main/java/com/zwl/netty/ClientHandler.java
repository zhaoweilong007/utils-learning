package com.zwl.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhaoWeiLong
 * @since 2021/7/26
 **/
@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {


  /**
   * 连接成功调用
   *
   * @param ctx
   * @throws Exception
   */
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    log.info("客户端写出数据");
    ByteBuf buffer = ctx.alloc().buffer();
    buffer.writeBytes((new Date() + ",hello world").getBytes(StandardCharsets.UTF_8));
    ctx.writeAndFlush(buffer);
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    ByteBuf byteBuf = (ByteBuf) msg;
    log.info("客户端读取到数据：{}", byteBuf.toString(StandardCharsets.UTF_8));
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    super.channelReadComplete(ctx);
  }
}
