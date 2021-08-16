package com.zwl.netty.im.client;

import com.zwl.netty.im.model.MessageRequestPacket;
import com.zwl.netty.im.utils.LogUtils;
import com.zwl.netty.im.utils.PacketCode;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;

/**
 * IM聊天室客户端
 *
 * @author ZhaoWeiLong
 * @since 2021/7/29
 **/
@Slf4j
public class ClientStart {


  public static void main(String[] args) {
    NioEventLoopGroup loopGroup = new NioEventLoopGroup();
    Bootstrap bootstrap = new Bootstrap();
    Bootstrap boot = bootstrap.group(loopGroup)
        .channel(NioSocketChannel.class)
        .option(ChannelOption.SO_KEEPALIVE, true)
        .option(ChannelOption.TCP_NODELAY, true)
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
        .handler(new ChannelInitializer<NioSocketChannel>() {
          @Override
          protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
            nioSocketChannel.pipeline().addLast(new ClientHandler());
          }
        });
    connect(boot, 3);
  }

  /**
   * 连接
   *
   * @param bootstrap 启动器
   * @param retry     重试次数
   */
  public static void connect(final Bootstrap bootstrap, final int retry) {
    bootstrap.connect("127.0.0.1", 8000).addListener(future -> {
      if (future.isSuccess()) {
        log.info("客户端连接成功....");
        Channel channel = ((ChannelFuture) future).channel();
        startConsoleThread(channel);
      } else {
        if (retry > 0) {
          connect(bootstrap, retry - 1);
        } else {
          log.error("连接服务器失败，超过重试次数");
        }
      }
    });
  }

  private static final ExecutorService EXECUTORS = Executors.newSingleThreadExecutor();

  /**
   * 开始读取用户输入
   *
   * @param channel
   */
  private static void startConsoleThread(Channel channel) {
    EXECUTORS.execute(() -> {
      while (!EXECUTORS.isShutdown()) {
        if (LogUtils.hasLogin(channel)) {
          log.info("输入消息:");
          Scanner scanner = new Scanner(System.in);
          String line = scanner.nextLine();
          MessageRequestPacket packet = new MessageRequestPacket();
          packet.setMessage(line);
          ByteBuf byteBuf = PacketCode.encode(channel.alloc().buffer(), packet);
          channel.writeAndFlush(byteBuf);
        }
      }
    });
  }

}
