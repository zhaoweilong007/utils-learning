package com.zwl.netty.im.client;

import com.zwl.netty.im.command.ConsoleCommandManager;
import com.zwl.netty.im.handler.CreateGroupRespHandler;
import com.zwl.netty.im.handler.LoginRespHandler;
import com.zwl.netty.im.handler.MessageRespHandler;
import com.zwl.netty.im.handler.PacketDecode;
import com.zwl.netty.im.handler.PacketEnCode;
import com.zwl.netty.im.handler.UnPackDeCoder;
import com.zwl.netty.im.utils.LogUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
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
public class IMClient {


  public static void main(String[] args) {
    NioEventLoopGroup loopGroup = new NioEventLoopGroup();
    Bootstrap bootstrap = new Bootstrap();
    Bootstrap boot = bootstrap.group(loopGroup)
        .channel(NioSocketChannel.class)
        .option(ChannelOption.SO_KEEPALIVE, true)
        .option(ChannelOption.TCP_NODELAY, true)
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
        .handler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel socketChannel) {
            //设置拆包，基于长度符的拆包器，根据自定义协议
            socketChannel.pipeline().addLast(new UnPackDeCoder());
            socketChannel.pipeline().addLast(new PacketDecode());
            socketChannel.pipeline().addLast(new LoginRespHandler());
            socketChannel.pipeline().addLast(new MessageRespHandler());
            socketChannel.pipeline().addLast(new CreateGroupRespHandler());
            socketChannel.pipeline().addLast(new PacketEnCode());
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
    ConsoleCommandManager consoleCommandManager = new ConsoleCommandManager();
    Scanner scanner = new Scanner(System.in);
    EXECUTORS.execute(() -> {
      while (!EXECUTORS.isShutdown() && channel.isActive()) {
        if (LogUtils.hasLogin(channel)) {
          consoleCommandManager.exec(scanner, channel);
        }
      }
    });
  }

}
