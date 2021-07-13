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

> 高性能队列

## netty

> 高性能的网路框架，在NIO的基础上对其封装的

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
