package com.zwl.netty.im.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
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
    bootstrap.group(loopGroup)
        .channel(NioSocketChannel.class)
        .option(ChannelOption.SO_KEEPALIVE, true)
        .option(ChannelOption.TCP_NODELAY, true)
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000)
        .handler(new ChannelInitializer<NioSocketChannel>() {
          @Override
          protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
            nioSocketChannel.pipeline().addLast(new ClientHandler());
          }
        }).connect("127.0.0.1", 8000);
    log.info("客户端连接成功....");

  }

}
