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
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    login(ctx.channel());
    super.channelActive(ctx);
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    log.info("客户端连接被关闭");
    super.channelInactive(ctx);
  }

  /**
   * 登录
   *
   * @param channel
   * @throws Exception
   */
  private void login(Channel channel) throws Exception {
    Scanner scanner = new Scanner(System.in);
    log.info("请输入账号登录系统。。。");
    String username = scanner.nextLine();
    log.info("请输入密码。。。");
    String pwd = scanner.nextLine();
    LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
    loginRequestPacket.setUserId(UUID.randomUUID().toString().replace("-", ""));
    loginRequestPacket.setUserName(username);
    loginRequestPacket.setPassword(pwd);
    ChannelFuture channelFuture = channel
        .writeAndFlush(loginRequestPacket);
    channelFuture.sync();
    if (!channelFuture.isSuccess()) {
      login(channel);
    }
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, LoginRespPacket loginRespPacket) {
    if (loginRespPacket.getSuccess()) {
      LogUtils.markAsLogin(ctx.channel());
    }
    log.info(loginRespPacket.getMsg());
  }
}
