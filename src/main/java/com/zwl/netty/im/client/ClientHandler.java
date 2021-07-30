package com.zwl.netty.im.client;

import com.zwl.netty.im.model.LoginRequestPacket;
import com.zwl.netty.im.model.LoginRespPacket;
import com.zwl.netty.im.model.Packet;
import com.zwl.netty.im.serialize.PacketCode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端处理器
 *
 * @author ZhaoWeiLong
 * @since 2021/7/29
 **/
@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {

  private String token;

  /**
   * 简历连接成功回调 发送登录请求
   *
   * @param ctx 上下文对象
   * @throws Exception
   */
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    log.info("客户端开始登录：{}", new Date());
    Scanner scanner = new Scanner(System.in);
    if (token == null) {
      log.info("请输入账号登录系统。。。");
      String username = scanner.nextLine();
      log.info("请输入密码。。。");
      String pwd = scanner.nextLine();
      LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
      loginRequestPacket.setUserId(UUID.randomUUID().toString().replace("-", ""));
      loginRequestPacket.setUserName(username);
      loginRequestPacket.setPassword(pwd);
      ctx.writeAndFlush(PacketCode.encode(ctx.alloc().buffer(), loginRequestPacket));
    }
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    Packet packet = PacketCode.decode(((ByteBuf) msg));
    int command = packet.getCommand().intValue();
    if (packet instanceof LoginRespPacket) {
      LoginRespPacket respPacket = (LoginRespPacket) packet;
      token = respPacket.getToken();
      log.info(respPacket.getMsg());
    }
  }
}
