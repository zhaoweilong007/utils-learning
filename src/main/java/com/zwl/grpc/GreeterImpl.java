package com.zwl.grpc;

import com.zwl.grpc.HelloResponse.Builder;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务实现类
 *
 * @author ZhaoWeiLong
 * @since 2021/8/26
 **/
@Slf4j
public class GreeterImpl extends GreeterGrpc.GreeterImplBase {


  @Override
  public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
    log.info("调用简单rpc....");
    responseObserver.onNext(parseResp(request));
    responseObserver.onCompleted();
  }

  @SneakyThrows
  @Override
  public void listSayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
    log.info("调用服务端流式rpc....");
    AtomicInteger ato = new AtomicInteger(0);
    Stream.generate(() -> {
      String msg = "hello " + request.getName() + ato.getAndIncrement();
      try {
        TimeUnit.SECONDS.sleep(2);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return HelloResponse.newBuilder().setMessage(msg).build();
    }).limit(5).forEach(responseObserver::onNext);

    responseObserver.onCompleted();
  }

  @Override
  public StreamObserver<HelloRequest> streamSayHello(
      StreamObserver<HelloResponse> responseObserver) {
    log.info("调用客户端流式rpc。。。。");
    return new StreamObserver<HelloRequest>() {

      List<HelloRequest> requests = new ArrayList<>();

      @Override
      public void onNext(HelloRequest request) {
        log.info("收到request：{}", request);
        requests.add(request);
      }

      @Override
      public void onError(Throwable throwable) {
        log.warn("发生错误：{}", throwable.getMessage());
      }

      @Override
      public void onCompleted() {
        log.info("客户端数据全部接收完成,requests:{}", requests);
        responseObserver.onNext(HelloResponse.newBuilder().setMessage("执行完成").build());
        responseObserver.onCompleted();
      }
    };

  }

  @Override
  public StreamObserver<HelloRequest> sayHelloAll(StreamObserver<HelloResponse> responseObserver) {
    log.info("双向流式rpc。。。 ");
    Builder builder = HelloResponse.newBuilder();
    return new StreamObserver<HelloRequest>() {
      @Override
      public void onNext(HelloRequest request) {
        log.info("接收客户端请求:{}", request);
        responseObserver.onNext(builder.setMessage("hello " + request.getName()).build());
        try {
          TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onError(Throwable throwable) {
      }

      @Override
      public void onCompleted() {
        log.info("数据接收完成");
        responseObserver.onCompleted();
      }
    };

  }

  private HelloResponse parseResp(HelloRequest request) {
    return HelloResponse.newBuilder().
        setMessage("hello " + request.getName()).build();
  }

}
