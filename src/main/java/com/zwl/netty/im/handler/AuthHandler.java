package com.zwl.netty.im.handler;

import com.zwl.netty.im.utils.LogUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * 认证处理器
 *
 * @author ZhaoWeiLong
 * @since 2021/8/18
 **/
@Slf4j
public class AuthHandler extends ChannelInboundHandlerAdapter {


  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    if (LogUtils.hasSession(ctx.channel())) {
      ctx.pipeline().remove(this);
      log.info("客户端已经登录。。移除authHandler");
      super.channelRead(ctx, msg);
    } else {
      log.info("客户端未登录。强制关闭连接");
      ctx.channel().close();
    }
  }

}
