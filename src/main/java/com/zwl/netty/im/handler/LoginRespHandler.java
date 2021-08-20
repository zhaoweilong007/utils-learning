package com.zwl.netty.im.handler;

import com.zwl.netty.im.model.LoginRequestPacket;
import com.zwl.netty.im.model.LoginRespPacket;
import com.zwl.netty.im.utils.LogUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.Scanner;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

/**
 * 、 登录响应处理器
 *
 * @author ZhaoWeiLong
 * @since 2021/8/17
 **/
@Slf4j
@Sharable
public class LoginRespHandler extends SimpleChannelInboundHandler<LoginRespPacket> {

  public static final LoginRespHandler INSTANCE = new LoginRespHandler();


  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    log.info("客户端连接被关闭");
    super.channelInactive(ctx);
  }


  @Override
  protected void channelRead0(ChannelHandlerContext ctx, LoginRespPacket loginRespPacket) {
    if (loginRespPacket.getSuccess()) {
      LogUtils.markAsLogin(ctx.channel());
    }
    log.info(loginRespPacket.getMsg());
  }
}
