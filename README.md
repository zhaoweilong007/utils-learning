# Utils leaning

> 整理各种工具类的使用

## excel

> excel读取和写入

- [excel测试](src/main/java/com/zwl/excel/ExcelTest.java)

## http

> apache http和okhttp

- [okhttp测试](src/main/java/com/zwl/http/okhttp/OkHttpTest.java)

- [httpclient测试](src/main/java/com/zwl/http/httpclient/HttpClientTest.java)

## json

> 序列化和反序列化

- [gson](src/main/java/com/zwl/json/gson/GsonTest.java)

- [fastjson](src/main/java/com/zwl/json/fastjson/JSONTest.java)

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

> 是一个高性能异步处理框架，轻量级的JMS，生产-消费模型，支持点对点和发布订阅


**等待策略**
- BlockingWaitStrategy 是最低效的策略，但其对 CPU 的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现；
  
- SleepingWaitStrategy 的性能表现跟 BlockingWaitStrategy 差不多，对 CPU 的消耗也类似，但其对生产者线程的影响最小，适合用于异步日志类似的场景；

- YieldingWaitStrategy 的性能是最好的，适合用于低延迟的系统。在要求极高性能且事件处理线数小于 CPU 逻辑核心数的场景中，推荐使用此策略；例如：CPU 开启超线程的特性。


- [disruptor案例](src/main/java/com/zwl/disruptor/DisruptorTest.java)

## netty

> 高性能的网路框架，在NIO的基础上对其封装的,NIO是同步非阻塞，面向缓冲流的IO


- [Netty测试案例](src/main/java/com/zwl/netty/NettyTest.java)


## jsoup

> 爬取html解析dom，用于网络爬取

### 爬取知乎所有话题下高赞回答

#### 运行方式

**导入MySQL**
运行sql文件中的两条语句。answer为答案表，存放所有答案，topic存放所有话题

**运行rocketmq**
在docker文件中，使用`docker-compose up`启动rocketmq，需要在broker.conf修改brokerIP1为本机地址

**启动spring应用**
启动后执行com.zwl.jsoup.WebCrawler的run方法，开始爬取知乎

[jsoup测试](src/main/java/com/zwl/jsoup/JsoupTest.java)

## mapstruct

[mapstruct测试](src/main/java/com/zwl/mapstruct/MapperTest.java)


> bean转换工具类

## hutool

> java工具类库

[hutool测试](src/main/java/com/zwl/hutool/HutoolTest.java)

## redission

> redis分布式锁

[redission测试](src/main/java/com/zwl/redis/RedissionTest.java)
