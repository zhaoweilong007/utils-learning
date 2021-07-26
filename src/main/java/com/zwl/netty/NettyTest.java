package com.zwl.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Netty基础使用
 * @author ZhaoWeiLong
 * @since 2021/7/26
 **/
@Slf4j
public class NettyTest {


  @Test
  public void startServer() throws InterruptedException {
    //简单的netty nio 服务端
    //定义组，相当于传统io的线程组,boss为新连接的线程组，work为读取数据线程组
    NioEventLoopGroup bossGroup = new NioEventLoopGroup();
    NioEventLoopGroup workGroup = new NioEventLoopGroup();

    ServerBootstrap serverBootstrap = new ServerBootstrap();
    //binding group
    serverBootstrap.group(bossGroup, workGroup)
        //指定模型为nio
        .channel(NioServerSocketChannel.class)
        //定义新连接的数据处理
        .childHandler(new ChannelInitializer<NioSocketChannel>() {
          //初始化channel
          @Override
          protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
            nioSocketChannel.pipeline().addLast(new StringDecoder());
            nioSocketChannel.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
              //数据读取
              @Override
              protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s)
                  throws Exception {
                log.info("读取数据：{}", s);
                ByteBuf buffer = channelHandlerContext.alloc().buffer();
                buffer.writeBytes(("我收到了你的数据：" + s).getBytes(StandardCharsets.UTF_8));
                channelHandlerContext.channel().writeAndFlush(buffer);
              }
            });
          }
        })
        //自定义属性
        .childAttr(AttributeKey.newInstance("serverName"), "nettyServer")
        //服务器channel设置，存放已完成三次握手的请求队列的最大长度
        .option(ChannelOption.SO_BACKLOG, 1024)
        //给每条连接设置
        //开启心跳检测
        .childOption(ChannelOption.SO_KEEPALIVE, true)
        //开启nagle算法
        .childOption(ChannelOption.TCP_NODELAY, true);
    //binding 端口
    log.info("开始绑定服务器端口.。。");
    bind(serverBootstrap, 1000);
    for (; ; ) {
    }
  }

  /**
   * 自动绑定端口
   *
   * @param serverBootstrap
   * @param port
   */
  private void bind(final ServerBootstrap serverBootstrap, final int port) {
    serverBootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
      @Override
      public void operationComplete(Future<? super Void> future) throws Exception {
        if (future.isSuccess()) {
          log.info("端口绑定成功，服务器正常启动 port:{}", port);
        } else {
          bind(serverBootstrap, port + 1);
        }
      }
    });
  }

  @Test
  public void startClient() {
    //netty client
    Bootstrap bootstrap = new Bootstrap();
    NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
    bootstrap.group(eventExecutors)
        .channel(NioSocketChannel.class)
        .option(ChannelOption.SO_KEEPALIVE, true)
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
        .option(ChannelOption.TCP_NODELAY, true)
        .handler(new ChannelInitializer<Channel>() {
          @Override
          protected void initChannel(Channel channel) throws Exception {
//            channel.pipeline().addLast(new StringEncoder());
            channel.pipeline().addLast(new ClientHandler());
          }
        });
    connect(bootstrap, "127.0.0.1", 1000, MAX_RETRY);
    for (; ; ) {
    }
  }

  public static final Integer MAX_RETRY = 10;

  /**
   * 重连
   *
   * @param bootstrap
   * @param host      主机
   * @param port      端口
   * @param retry     重试次数
   */
  private void connect(Bootstrap bootstrap, String host, int port, int retry) {
    bootstrap.connect(host, port).addListener(
        future -> {
          if (future.isSuccess()) {
            log.info("连接服务器成功");
          } else if (retry == 0) {
            log.error("重试次数用完，连接失败");
          } else {
            int order = MAX_RETRY - retry + 1;
            //时间间隔
            int delay = 1 << order;
            log.info("{} ：连接失败，第：{}次重连...", new Date(), retry);
            bootstrap.config().group().schedule(() -> {
              connect(bootstrap, host, port, retry - 1);
            }, delay, TimeUnit.SECONDS);
          }
        });
  }

}
