package com.zwl.netty.im.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端处理器
 *
 * @author ZhaoWeiLong
 * @since 2021/7/29
 **/
@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {

  /**
   * 简历连接成功回调 发送登录请求
   *
   * @param ctx 上下文对象
   * @throws Exception
   */
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    super.channelActive(ctx);
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {

  }
}
