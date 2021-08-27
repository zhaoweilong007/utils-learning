package com.zwl.grpc;


import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.zwl.grpc.GreeterGrpc.GreeterBlockingStub;
import com.zwl.grpc.GreeterGrpc.GreeterFutureStub;
import com.zwl.grpc.GreeterGrpc.GreeterStub;
import com.zwl.grpc.HelloRequest.Builder;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * grpc调用示例
 * <p>
 * grpc四种调用方式 简单rpc、服务端流式rpc、客户端流式rpc、双向rpc
 *
 * @author ZhaoWeiLong
 * @since 2021/8/26
 **/
@Slf4j
public class GRPCTest {

  static class Server {

    public static void main(String[] args) {
      try {
        io.grpc.Server server = ServerBuilder.forPort(8080).addService(new GreeterImpl())
            .build().start();
        log.info("server start listening on 8080");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
          log.info("jvm shutdown now,close grpc server");
          server.shutdown();
        }));
        //等待终止
        server.awaitTermination();
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  static class Client {

    public static ManagedChannel managedChannel;
    public static GreeterBlockingStub blockingStub;
    public static GreeterFutureStub futureStub;
    public static GreeterStub greeterStub;

    static HelloRequest helloRequest = HelloRequest.newBuilder().setName("grpc").build();

    static {
      //构建通道，通道是安全可复用的
      managedChannel = ManagedChannelBuilder.forTarget("localhost:8080").usePlaintext().build();
      //同步代理stub
      blockingStub = GreeterGrpc.newBlockingStub(managedChannel);
      //异步代理stub
      futureStub = GreeterGrpc.newFutureStub(managedChannel);
      greeterStub = GreeterGrpc.newStub(managedChannel);
    }

    /**
     * 简单示例
     */
    private static void simple() {
      log.info("simple<<<<<<<<<<<<<<<<<<<<");
      HelloResponse response = blockingStub.sayHello(helloRequest);
      log.info("sayHello response:{}", response);
    }

    /**
     * 服务端流式rpc示例
     */
    private static void serverRpc() {
      log.info("serverRpc<<<<<<<<<<<<<<<<<<<<<<<");
      Iterator<HelloResponse> sayHelloStream = blockingStub.listSayHello(helloRequest);
      sayHelloStream.forEachRemaining(helloResponse -> {
        log.info("listSayHello response:{} ", helloResponse);
      });
    }

    /**
     * 客户端流式rpc示例
     */
    private static void clientRpc() throws InterruptedException {
      log.info("clientRpc<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
      CountDownLatch latch = new CountDownLatch(1);
      StreamObserver<HelloRequest> requestStreamObserver = greeterStub.streamSayHello(
          new StreamObserver<HelloResponse>() {
            @Override
            public void onNext(HelloResponse helloResponse) {
              log.info("收到服务器响应：{}", helloResponse);
            }

            @Override
            public void onError(Throwable throwable) {
              log.info("发生错误：{}", throwable.getMessage());
              latch.countDown();
            }

            @Override
            public void onCompleted() {
              log.info("服务端数据全部接收完成");
              latch.countDown();
            }
          });

      AtomicInteger count = new AtomicInteger(1);
      Stream.generate(
          () -> {
            HelloRequest request = HelloRequest.newBuilder()
                .setName("测试客户端流式rpc req" + count.getAndIncrement()).build();
            try {
              TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            return request;
          }).limit(5).forEach(requestStreamObserver::onNext);
      requestStreamObserver.onCompleted();
      latch.await(1, TimeUnit.MINUTES);
    }


    /**
     * 双向rpc示例
     */
    private static void bothRpc() throws InterruptedException {
      log.info("bothRpc<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
      CountDownLatch downLatch = new CountDownLatch(1);
      StreamObserver<HelloRequest> requestStreamObserver = greeterStub.sayHelloAll(
          new StreamObserver<HelloResponse>() {
            @Override
            public void onNext(HelloResponse helloResponse) {
              log.info("收到服务器response:{}", helloResponse);
            }

            @Override
            public void onError(Throwable throwable) {
              log.warn("发生错误：{}", throwable.getMessage());
              downLatch.countDown();
            }

            @Override
            public void onCompleted() {
              log.info("服务器数据接收完成");
              downLatch.countDown();
            }
          });

      Builder builder = HelloRequest.newBuilder();
      for (int i = 0; i < 5; i++) {
        requestStreamObserver.onNext(builder.setName("rpc " + i).build());
      }
      requestStreamObserver.onCompleted();
      downLatch.await();
    }

    /**
     * 异步rpc示例
     */
    private static void asyncRpc() {
      log.info("asyncRpc<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
      ListenableFuture<HelloResponse> listenableFuture = futureStub.sayHello(
          helloRequest);
      //添加回调
      Futures.addCallback(listenableFuture, new FutureCallback<>() {
        @Override
        public void onSuccess(@Nullable HelloResponse helloResponse) {
          log.info("异步执行成功。。。{}", helloResponse);
        }

        @Override
        public void onFailure(Throwable throwable) {
          log.info("异步执行失败。。。{}", throwable.getMessage());
        }
      }, MoreExecutors.directExecutor());
    }

    public static void main(String[] args) {
      try {
        //简单rpc
        simple();

        //服务端流式rpc
        serverRpc();

        //异步执行
        asyncRpc();

        //客户端流式rpc
        clientRpc();

        //双向流式rpc
        bothRpc();

      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        if (managedChannel != null) {
          managedChannel.shutdown();
        }
      }

    }
  }

}
