package com.zwl.netty.im.server;

import com.zwl.netty.im.handler.AuthHandler;
import com.zwl.netty.im.handler.CreateGroupRequestHandler;
import com.zwl.netty.im.handler.GroupMessageReqHandler;
import com.zwl.netty.im.handler.IMHandler;
import com.zwl.netty.im.handler.JoinGroupReqHandler;
import com.zwl.netty.im.handler.ListGroupReqHandler;
import com.zwl.netty.im.handler.LoginOutRequestHandler;
import com.zwl.netty.im.handler.LoginRequestHandler;
import com.zwl.netty.im.handler.MessageRequestHandler;
import com.zwl.netty.im.handler.PacketCodecHandler;
import com.zwl.netty.im.handler.QuitGroupReqHandler;
import com.zwl.netty.im.handler.UnPackDeCoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.InetSocketAddress;
import lombok.extern.slf4j.Slf4j;

/**
 * IM聊天室服务端
 *
 * @author ZhaoWeiLong
 * @since 2021/7/29
 **/
@Slf4j
public class IMServer {

  public static void main(String[] args) {

    NioEventLoopGroup bossGroup = new NioEventLoopGroup();
    NioEventLoopGroup workGroup = new NioEventLoopGroup();

    ServerBootstrap serverBootstrap = new ServerBootstrap();

    try {
      serverBootstrap
          .group(bossGroup, workGroup)
          .localAddress(new InetSocketAddress(8000))
          .channel(NioServerSocketChannel.class)
          .childOption(ChannelOption.SO_KEEPALIVE, true)
          .childOption(ChannelOption.TCP_NODELAY, true)
          .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
              socketChannel.pipeline().addLast(new UnPackDeCoder());
              socketChannel.pipeline().addLast(PacketCodecHandler.INSTANCE);
              socketChannel.pipeline().addLast(LoginRequestHandler.INSTANCE);
              socketChannel.pipeline().addLast(AuthHandler.INSTANCE);
              socketChannel.pipeline().addLast(IMHandler.INSTANCE);
            }
          });

      ChannelFuture channelFuture = serverBootstrap.bind().sync();
      log.info("{}服务器启动成功 监听端口：{}", IMServer.class.getName(),
          channelFuture.channel().localAddress());
      ChannelFuture closeFuture = channelFuture.channel().closeFuture();
      closeFuture.sync();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      workGroup.shutdownGracefully();
      bossGroup.shutdownGracefully();
    }

  }

}
