package com.zwl.jsoup;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.zwl.jsoup.mapper.AnswerMapper;
import com.zwl.jsoup.mapper.TopicMapper;
import com.zwl.jsoup.model.Topic;
import com.zwl.jsoup.service.AnswerService;
import com.zwl.jsoup.service.TopicService;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.ResponseBody;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 爬取知乎所有话题神评
 *
 * @author zhao_wei_long
 * @since 2021/6/23
 **/
@Slf4j
@Component
@Configuration
public class WebCrawler implements CommandLineRunner {


  @Autowired
  TopicMapper topicMapper;

  @Autowired
  TopicService topicService;

  @Autowired
  AnswerMapper answerMapper;

  @Autowired
  AnswerService answerService;

  @Autowired
  OkHttpClient client;

  @Autowired
  private ListeningExecutorService listeningExecutorService;

  @Autowired
  RocketMQTemplate rocketMQTemplate;

  private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36";
  private static final String TOPICLIST = "https://www.zhihu.com/node/TopicsPlazzaListV2";
  private static final String TOPICURL = "https://www.zhihu.com/topics";
  private static final String ANSWERURL = "https://www.zhihu.com/api/v4/topics/19860414/feeds/essence?limit=50&offset=%d";
  private static final String ANSWER_FEED = "https://www.zhihu.com/question/%d/answer/%d";


  @Override
  public void run(String... args) throws Exception {
    boolean loadFromDBOnTopic = true;
//    startCrawlingZhiHu(loadFromDBOnTopic);
  }


  /**
   * 查询所有子话题
   *
   * @param parentId 父话题id
   */
  private ConcurrentLinkedQueue<Topic> queryTopicChildren(Integer parentId) {
    ConcurrentLinkedQueue<Topic> topics = new ConcurrentLinkedQueue<>();
    int offset = 0;
    for (; ; ) {
      log.info("查询父话题：【{}】下，offset=【{}】的子话题", parentId, offset);
      FormBody body = new FormBody.Builder()
          .add("method", "next")
          .add("params",
              "{\"topic_id\":" + parentId + ",\"offset\":" + offset + ",\"hash_id\":\"\"}")
          .build();
      Request request = new Builder().url(TOPICLIST).post(body).build();
      String json;
      try (ResponseBody responseBody = client.newCall(request).execute().body()) {
        if (responseBody == null) {
          break;
        }
        json = responseBody.string();
      } catch (IOException e) {
        return topics;
      }
      JSONObject jsonObject = JSON.parseObject(json);
      JSONArray msg = jsonObject.getJSONArray("msg");
      if (msg == null || msg.isEmpty()) {
        return topics;
      }
      ConcurrentLinkedQueue<Topic> collect = msg.parallelStream().map(o -> {
        Document document = Jsoup.parse(((String) o));
        Element element = document.select("a[target=_blank]").first();
        String comment = document.getElementsByTag("p").first().text();
        String href = element.attr("href");
        String tid = StrUtil.subAfter(href, "/topic/", true);
        String desc = element.select("strong").text();
        Topic topic = new Topic();
        topic.setTopicId(Integer.parseInt(tid));
        topic.setTopicName(desc);
        topic.setParentId(Convert.toLong(parentId));
        topic.setComment(comment);
        return topic;
      }).collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
      topics.addAll(collect);
      offset += 20;
    }
    return topics;
  }

  /**
   * 提柜解析所有话题
   *
   * @param topics 当前话题
   */
  private void parseTopic(@NotNull ConcurrentLinkedQueue<Topic> topics) {
    topics.forEach(topic -> {
          rocketMQTemplate.convertAndSend("queryAnswer", topic);
          listeningExecutorService.execute(() -> {
            long count = topicMapper
                .selectCount(
                    Wrappers.<Topic>lambdaQuery().eq(Topic::getTopicId, topic.getTopicId()));
            if (count == 0) {
              topicMapper.insert(topic);
            }
          });
          try {
            ConcurrentLinkedQueue<Topic> topicChildren = listeningExecutorService
                .submit(() -> queryTopicChildren(topic.getTopicId())).get();
            if (!topicChildren.isEmpty()) {
              parseTopic(topicChildren);
            }
          } catch (InterruptedException | ExecutionException e) {
            log.error("查询子话题：【{}】失败，msg:{}", topic.getTopicName(), e.getMessage());
          }
        }
    );
  }

  /**
   * 获取根话题
   *
   * @return 跟话题集合
   */
  @NotNull
  private ConcurrentLinkedQueue<Topic> getRootTopic() throws IOException {
    log.info("》》》》》》》》》》》》》》》开始查询根话题《《《《《《《《《《《《《《《《《《");
    Document document = Jsoup.connect(TOPICURL)
        .userAgent(USER_AGENT)
        .get();
    Elements elements = document.select(".zm-topic-cat-item");
    ConcurrentLinkedQueue<Topic> queue = elements.stream()
        .map(element -> {
          Topic topic = new Topic();
          topic.setTopicId(Integer.parseInt(element.attr("data-id")));
          topic.setTopicName(element.child(0).text());
          return topic;
        })
        .collect(Collectors.toCollection(ConcurrentLinkedQueue::new));

    List<Topic> topics = queue.parallelStream().filter(topic -> {
      long one = topicMapper
          .selectCount(Wrappers.<Topic>lambdaQuery().eq(Topic::getTopicId, topic.getTopicId()));
      return one == 0;
    }).collect(Collectors.toList());
    topicService.saveBatch(topics);
    return queue;
  }


  /**
   * 获取所有话题列表
   *
   * @param loadFromDBOnTopic 是否从数据库加载topic
   */
  private void startCrawlingZhiHu(Boolean loadFromDBOnTopic) throws Exception {
    log.info(">>>>>>>>>>>>>>>>>>>>>>>>>开始获取所有根话题列表>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    List<Topic> topics = topicService
        .list(Wrappers.<Topic>lambdaQuery().eq(Topic::getParentId, 0));
    ConcurrentLinkedQueue<Topic> rootTopic;
    if (topics.isEmpty()) {
      rootTopic = getRootTopic();
    } else {
      rootTopic = new ConcurrentLinkedQueue<>(topics);
    }
    log.info(">>>>>>>>>>>>>>>>>>>>>>>>>开始解析所有跟话题下子话题>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    if (loadFromDBOnTopic) {
      List<Topic> topicList = topicService
          .list(Wrappers.<Topic>lambdaQuery().orderBy(true, true, Topic::getTopicId));
      topicList.forEach(topic -> rocketMQTemplate.convertAndSend("queryAnswer", topic));
      log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>事件全部推动完成<<<<<<<<<<<<<<<<<<<<<<<");
    } else {
      parseTopic(rootTopic);
    }
  }

}
