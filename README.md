<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Utils leaning](#utils-leaning)
  - [excel](#excel)
  - [http](#http)
  - [json](#json)
  - [protocol buffer](#protocol-buffer)
  - [guava](#guava)
  - [disruptor](#disruptor)
  - [netty](#netty)
    - [IM聊天室](#im%E8%81%8A%E5%A4%A9%E5%AE%A4)
  - [jsoup](#jsoup)
    - [爬取知乎所有话题下高赞回答](#%E7%88%AC%E5%8F%96%E7%9F%A5%E4%B9%8E%E6%89%80%E6%9C%89%E8%AF%9D%E9%A2%98%E4%B8%8B%E9%AB%98%E8%B5%9E%E5%9B%9E%E7%AD%94)
  - [mapstruct](#mapstruct)
  - [hutool](#hutool)
  - [redission](#redission)
  - [retrofit](#retrofit)
  - [grpc](#grpc)
    - [基础使用](#%E5%9F%BA%E7%A1%80%E4%BD%BF%E7%94%A8)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Utils leaning

> 整理各种工具类的使用

## excel

> excel读取和写入,阿里的easyExcel

- [excel测试](src/main/java/com/zwl/excel/ExcelTest.java)

## http

> apache http和okhttp

- [okhttp测试](src/main/java/com/zwl/http/okhttp/OkHttpTest.java)

- [httpclient测试](src/main/java/com/zwl/http/httpclient/HttpClientTest.java)

## json

> 序列化和反序列化,主要使用google的gson和阿里的fastjson

- [gson](src/main/java/com/zwl/json/gson/GsonTest.java)

- [fastjson](src/main/java/com/zwl/json/fastjson/JSONTest.java)

## protocol buffer

> 高性能的序列化工具，实现自动编码、解码，体积和速度比xml和json更小、更快，由google开源

- [protocol buffer 测试](src/main/java/com/zwl/protocolbuffer/ProtocTest.java)

## guava

> google的java类库，提供了大量的工具类

- [基础测试](src/main/java/com/zwl/guava/base/BaseTest.java)
- [缓存测试](src/main/java/com/zwl/guava/cache/CacheTest.java)
- [集合测试](src/main/java/com/zwl/guava/collection/CollectionTest.java)
- [函数测试](src/main/java/com/zwl/guava/function/FunctionTest.java)
- [反射测试](src/main/java/com/zwl/guava/reflect/ReflectTest.java)
- [字符处理测试](src/main/java/com/zwl/guava/str/StrTest.java)
- [并发测试](src/main/java/com/zwl/guava/concurrent/ConcurrentTest.java)
- [事件总线测试](src/main/java/com/zwl/guava/eventbus/EventBusTest.java)

## disruptor

> 是美团开源的一个高性能异步处理框架，轻量级的JMS，生产-消费模型，支持点对点和发布订阅，高性能队列


**等待策略**

- BlockingWaitStrategy 是最低效的策略，但其对 CPU 的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现；

- SleepingWaitStrategy 的性能表现跟 BlockingWaitStrategy 差不多，对 CPU 的消耗也类似，但其对生产者线程的影响最小，适合用于异步日志类似的场景；

- YieldingWaitStrategy 的性能是最好的，适合用于低延迟的系统。在要求极高性能且事件处理线数小于 CPU 逻辑核心数的场景中，推荐使用此策略；例如：CPU 开启超线程的特性。


- [disruptor案例](src/main/java/com/zwl/disruptor/DisruptorTest.java)

## netty

> 高性能的网路框架，在NIO的基础上对其封装的,NIO是同步非阻塞，面向缓冲流的IO

- [Netty测试案例](src/main/java/com/zwl/netty/NettyTest.java)

### IM聊天室

**完成功能**

- 自定义协议
- 自定义协议编解码
- 根据协议使用拆包器
- 拒绝非本协议连接
- 心跳检测机制
- 合并handler和并行handler
- 登录退出
- 单聊消息
- 群聊消息
- 创建群聊
- 拉人群聊
- 列出群聊
- 退出群聊

> 默认用户：账号密码相同:admin,root

[IM聊天室服务端 Server启动类](src/main/java/com/zwl/netty/im/server/IMServer.java)

[IM聊天室客户端 Client启动类](src/main/java/com/zwl/netty/im/client/IMClient.java)

## jsoup

> 爬取html解析dom，用于网络爬取

### 爬取知乎所有话题下高赞回答

- 运行方式

**导入MySQL**
运行sql文件中的两条语句。answer为答案表，存放所有答案，topic存放所有话题

**运行rocketmq**
在docker文件中，使用`docker-compose up`启动rocketmq，需要在broker.conf修改brokerIP1为本机地址

**启动spring应用**
启动后执行com.zwl.jsoup.WebCrawler的run方法，开始爬取知乎

[jsoup测试](src/main/java/com/zwl/jsoup/JsoupTest.java)

## mapstruct

> bean转换工具类，比如DTO转换entity，entity转换VO等等

[mapstruct测试](src/main/java/com/zwl/mapstruct/MapperTest.java)

## hutool

> java工具类库

[hutool测试](src/main/java/com/zwl/hutool/HutoolTest.java)

## redission

> redis分布式锁

[redission测试](src/main/java/com/zwl/redis/RedissionTest.java)

## retrofit

> [retrofit](https://github.com/square/retrofit) A type-safe HTTP client for Android and Java

[retrofit测试](src/main/java/com/zwl/retrofit/RetroTest.java)

## grpc

> [gRPC](https://grpc.io/) 是一个现代的高性能、开源远程过程调用 (RPC) 框架，可以在任何地方运行。它使客户端和服务器应用程序能够透明地通信，并使构建连接系统变得更加容易。
> grpc使用protobuf序列化协议


**主要使用场景**

- 低延迟、高度可扩展的分布式系统。
- 开发与云服务器通信的移动客户端。
- 设计一个需要准确、高效且独立于语言的新协议。
- 分层设计以启用扩展，例如。身份验证、负载平衡、日志记录和监控等

### 基础使用

建议查看官网教程<https://grpc.io/docs/languages/java/basics/>

使用[grpc-java](https://github.com/grpc/grpc-java/blob/master/README.md) 类库在proto文件中定义rpc服务

```protobuf

//定义rpc服务
service Greeter{
  //简单的一元rpc
  rpc sayHello (HelloRequest) returns (HelloResponse) {}

  //服务器流式rpc，客户端从返回的流中读取，直到没有消息
  rpc listSayHello (HelloRequest) returns (stream HelloResponse) {}

  //客户端流式rpc，客户端写入消息，等待服务端读取消息返回响应
  rpc streamSayHello (stream HelloRequest) returns (HelloResponse) {}

  //双向流式rpc
  rpc sayHelloAll (stream HelloRequest) returns (stream HelloResponse) {}
}

message HelloRequest{
  string name = 1;
}

message HelloResponse{
  string message = 1;
}
```

使用maven插件生成客户端和服务端代码

[GRPC测试案例](src/main/java/com/zwl/grpc/GRPCTest.java)

