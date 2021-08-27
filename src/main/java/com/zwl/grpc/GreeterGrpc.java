package com.zwl.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 *定义rpc服务
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.40.1)",
    comments = "Source: Service.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class GreeterGrpc {

  private GreeterGrpc() {}

  public static final String SERVICE_NAME = "com.zwl.grpc.Greeter";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.zwl.grpc.HelloRequest,
      com.zwl.grpc.HelloResponse> getSayHelloMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "sayHello",
      requestType = com.zwl.grpc.HelloRequest.class,
      responseType = com.zwl.grpc.HelloResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.zwl.grpc.HelloRequest,
      com.zwl.grpc.HelloResponse> getSayHelloMethod() {
    io.grpc.MethodDescriptor<com.zwl.grpc.HelloRequest, com.zwl.grpc.HelloResponse> getSayHelloMethod;
    if ((getSayHelloMethod = GreeterGrpc.getSayHelloMethod) == null) {
      synchronized (GreeterGrpc.class) {
        if ((getSayHelloMethod = GreeterGrpc.getSayHelloMethod) == null) {
          GreeterGrpc.getSayHelloMethod = getSayHelloMethod =
              io.grpc.MethodDescriptor.<com.zwl.grpc.HelloRequest, com.zwl.grpc.HelloResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "sayHello"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zwl.grpc.HelloRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zwl.grpc.HelloResponse.getDefaultInstance()))
              .setSchemaDescriptor(new GreeterMethodDescriptorSupplier("sayHello"))
              .build();
        }
      }
    }
    return getSayHelloMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.zwl.grpc.HelloRequest,
      com.zwl.grpc.HelloResponse> getListSayHelloMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "listSayHello",
      requestType = com.zwl.grpc.HelloRequest.class,
      responseType = com.zwl.grpc.HelloResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<com.zwl.grpc.HelloRequest,
      com.zwl.grpc.HelloResponse> getListSayHelloMethod() {
    io.grpc.MethodDescriptor<com.zwl.grpc.HelloRequest, com.zwl.grpc.HelloResponse> getListSayHelloMethod;
    if ((getListSayHelloMethod = GreeterGrpc.getListSayHelloMethod) == null) {
      synchronized (GreeterGrpc.class) {
        if ((getListSayHelloMethod = GreeterGrpc.getListSayHelloMethod) == null) {
          GreeterGrpc.getListSayHelloMethod = getListSayHelloMethod =
              io.grpc.MethodDescriptor.<com.zwl.grpc.HelloRequest, com.zwl.grpc.HelloResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "listSayHello"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zwl.grpc.HelloRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zwl.grpc.HelloResponse.getDefaultInstance()))
              .setSchemaDescriptor(new GreeterMethodDescriptorSupplier("listSayHello"))
              .build();
        }
      }
    }
    return getListSayHelloMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.zwl.grpc.HelloRequest,
      com.zwl.grpc.HelloResponse> getStreamSayHelloMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "streamSayHello",
      requestType = com.zwl.grpc.HelloRequest.class,
      responseType = com.zwl.grpc.HelloResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
  public static io.grpc.MethodDescriptor<com.zwl.grpc.HelloRequest,
      com.zwl.grpc.HelloResponse> getStreamSayHelloMethod() {
    io.grpc.MethodDescriptor<com.zwl.grpc.HelloRequest, com.zwl.grpc.HelloResponse> getStreamSayHelloMethod;
    if ((getStreamSayHelloMethod = GreeterGrpc.getStreamSayHelloMethod) == null) {
      synchronized (GreeterGrpc.class) {
        if ((getStreamSayHelloMethod = GreeterGrpc.getStreamSayHelloMethod) == null) {
          GreeterGrpc.getStreamSayHelloMethod = getStreamSayHelloMethod =
              io.grpc.MethodDescriptor.<com.zwl.grpc.HelloRequest, com.zwl.grpc.HelloResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "streamSayHello"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zwl.grpc.HelloRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zwl.grpc.HelloResponse.getDefaultInstance()))
              .setSchemaDescriptor(new GreeterMethodDescriptorSupplier("streamSayHello"))
              .build();
        }
      }
    }
    return getStreamSayHelloMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.zwl.grpc.HelloRequest,
      com.zwl.grpc.HelloResponse> getSayHelloAllMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "sayHelloAll",
      requestType = com.zwl.grpc.HelloRequest.class,
      responseType = com.zwl.grpc.HelloResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.zwl.grpc.HelloRequest,
      com.zwl.grpc.HelloResponse> getSayHelloAllMethod() {
    io.grpc.MethodDescriptor<com.zwl.grpc.HelloRequest, com.zwl.grpc.HelloResponse> getSayHelloAllMethod;
    if ((getSayHelloAllMethod = GreeterGrpc.getSayHelloAllMethod) == null) {
      synchronized (GreeterGrpc.class) {
        if ((getSayHelloAllMethod = GreeterGrpc.getSayHelloAllMethod) == null) {
          GreeterGrpc.getSayHelloAllMethod = getSayHelloAllMethod =
              io.grpc.MethodDescriptor.<com.zwl.grpc.HelloRequest, com.zwl.grpc.HelloResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "sayHelloAll"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zwl.grpc.HelloRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.zwl.grpc.HelloResponse.getDefaultInstance()))
              .setSchemaDescriptor(new GreeterMethodDescriptorSupplier("sayHelloAll"))
              .build();
        }
      }
    }
    return getSayHelloAllMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static GreeterStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GreeterStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GreeterStub>() {
        @java.lang.Override
        public GreeterStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GreeterStub(channel, callOptions);
        }
      };
    return GreeterStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static GreeterBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GreeterBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GreeterBlockingStub>() {
        @java.lang.Override
        public GreeterBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GreeterBlockingStub(channel, callOptions);
        }
      };
    return GreeterBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static GreeterFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GreeterFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GreeterFutureStub>() {
        @java.lang.Override
        public GreeterFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GreeterFutureStub(channel, callOptions);
        }
      };
    return GreeterFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   *定义rpc服务
   * </pre>
   */
  public static abstract class GreeterImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     *简单的一元rpc
     * </pre>
     */
    public void sayHello(com.zwl.grpc.HelloRequest request,
        io.grpc.stub.StreamObserver<com.zwl.grpc.HelloResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSayHelloMethod(), responseObserver);
    }

    /**
     * <pre>
     *服务器流式rpc，客户端从返回的流中读取，直到没有消息
     * </pre>
     */
    public void listSayHello(com.zwl.grpc.HelloRequest request,
        io.grpc.stub.StreamObserver<com.zwl.grpc.HelloResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListSayHelloMethod(), responseObserver);
    }

    /**
     * <pre>
     *客户端流式rpc，客户端写入消息，等待服务端读取消息返回响应
     * </pre>
     */
    public io.grpc.stub.StreamObserver<com.zwl.grpc.HelloRequest> streamSayHello(
        io.grpc.stub.StreamObserver<com.zwl.grpc.HelloResponse> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getStreamSayHelloMethod(), responseObserver);
    }

    /**
     * <pre>
     *双向流式rpc
     * </pre>
     */
    public io.grpc.stub.StreamObserver<com.zwl.grpc.HelloRequest> sayHelloAll(
        io.grpc.stub.StreamObserver<com.zwl.grpc.HelloResponse> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getSayHelloAllMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSayHelloMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.zwl.grpc.HelloRequest,
                com.zwl.grpc.HelloResponse>(
                  this, METHODID_SAY_HELLO)))
          .addMethod(
            getListSayHelloMethod(),
            io.grpc.stub.ServerCalls.asyncServerStreamingCall(
              new MethodHandlers<
                com.zwl.grpc.HelloRequest,
                com.zwl.grpc.HelloResponse>(
                  this, METHODID_LIST_SAY_HELLO)))
          .addMethod(
            getStreamSayHelloMethod(),
            io.grpc.stub.ServerCalls.asyncClientStreamingCall(
              new MethodHandlers<
                com.zwl.grpc.HelloRequest,
                com.zwl.grpc.HelloResponse>(
                  this, METHODID_STREAM_SAY_HELLO)))
          .addMethod(
            getSayHelloAllMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                com.zwl.grpc.HelloRequest,
                com.zwl.grpc.HelloResponse>(
                  this, METHODID_SAY_HELLO_ALL)))
          .build();
    }
  }

  /**
   * <pre>
   *定义rpc服务
   * </pre>
   */
  public static final class GreeterStub extends io.grpc.stub.AbstractAsyncStub<GreeterStub> {
    private GreeterStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GreeterStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GreeterStub(channel, callOptions);
    }

    /**
     * <pre>
     *简单的一元rpc
     * </pre>
     */
    public void sayHello(com.zwl.grpc.HelloRequest request,
        io.grpc.stub.StreamObserver<com.zwl.grpc.HelloResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSayHelloMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *服务器流式rpc，客户端从返回的流中读取，直到没有消息
     * </pre>
     */
    public void listSayHello(com.zwl.grpc.HelloRequest request,
        io.grpc.stub.StreamObserver<com.zwl.grpc.HelloResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getListSayHelloMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *客户端流式rpc，客户端写入消息，等待服务端读取消息返回响应
     * </pre>
     */
    public io.grpc.stub.StreamObserver<com.zwl.grpc.HelloRequest> streamSayHello(
        io.grpc.stub.StreamObserver<com.zwl.grpc.HelloResponse> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncClientStreamingCall(
          getChannel().newCall(getStreamSayHelloMethod(), getCallOptions()), responseObserver);
    }

    /**
     * <pre>
     *双向流式rpc
     * </pre>
     */
    public io.grpc.stub.StreamObserver<com.zwl.grpc.HelloRequest> sayHelloAll(
        io.grpc.stub.StreamObserver<com.zwl.grpc.HelloResponse> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getSayHelloAllMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   * <pre>
   *定义rpc服务
   * </pre>
   */
  public static final class GreeterBlockingStub extends io.grpc.stub.AbstractBlockingStub<GreeterBlockingStub> {
    private GreeterBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GreeterBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GreeterBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     *简单的一元rpc
     * </pre>
     */
    public com.zwl.grpc.HelloResponse sayHello(com.zwl.grpc.HelloRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSayHelloMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *服务器流式rpc，客户端从返回的流中读取，直到没有消息
     * </pre>
     */
    public java.util.Iterator<com.zwl.grpc.HelloResponse> listSayHello(
        com.zwl.grpc.HelloRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getListSayHelloMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   *定义rpc服务
   * </pre>
   */
  public static final class GreeterFutureStub extends io.grpc.stub.AbstractFutureStub<GreeterFutureStub> {
    private GreeterFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GreeterFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GreeterFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     *简单的一元rpc
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.zwl.grpc.HelloResponse> sayHello(
        com.zwl.grpc.HelloRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSayHelloMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SAY_HELLO = 0;
  private static final int METHODID_LIST_SAY_HELLO = 1;
  private static final int METHODID_STREAM_SAY_HELLO = 2;
  private static final int METHODID_SAY_HELLO_ALL = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final GreeterImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(GreeterImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SAY_HELLO:
          serviceImpl.sayHello((com.zwl.grpc.HelloRequest) request,
              (io.grpc.stub.StreamObserver<com.zwl.grpc.HelloResponse>) responseObserver);
          break;
        case METHODID_LIST_SAY_HELLO:
          serviceImpl.listSayHello((com.zwl.grpc.HelloRequest) request,
              (io.grpc.stub.StreamObserver<com.zwl.grpc.HelloResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_STREAM_SAY_HELLO:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.streamSayHello(
              (io.grpc.stub.StreamObserver<com.zwl.grpc.HelloResponse>) responseObserver);
        case METHODID_SAY_HELLO_ALL:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.sayHelloAll(
              (io.grpc.stub.StreamObserver<com.zwl.grpc.HelloResponse>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class GreeterBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    GreeterBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.zwl.grpc.Service.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Greeter");
    }
  }

  private static final class GreeterFileDescriptorSupplier
      extends GreeterBaseDescriptorSupplier {
    GreeterFileDescriptorSupplier() {}
  }

  private static final class GreeterMethodDescriptorSupplier
      extends GreeterBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    GreeterMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (GreeterGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new GreeterFileDescriptorSupplier())
              .addMethod(getSayHelloMethod())
              .addMethod(getListSayHelloMethod())
              .addMethod(getStreamSayHelloMethod())
              .addMethod(getSayHelloAllMethod())
              .build();
        }
      }
    }
    return result;
  }
}
