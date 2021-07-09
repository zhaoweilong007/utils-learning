package com.zwl.jsoup.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.RateLimiter;
import com.zwl.jsoup.model.Answer;
import com.zwl.jsoup.model.Topic;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.ResponseBody;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 查询回答
 *
 * @author ZhaoWeiLong
 * @since 2021/7/9
 **/
@Service
@RocketMQMessageListener(topic = "queryAnswer", consumerGroup = "queryAnswer-consumer")
@Slf4j
public class QueryAnswerConsumer implements RocketMQListener<Topic> {

  private static final String ANSWERURL = "https://www.zhihu.com/api/v4/topics/19860414/feeds/essence?limit=50&offset=%d";
  private static final String ANSWER_FEED = "https://www.zhihu.com/question/%d/answer/%d";

  @Autowired
  OkHttpClient client;

  @Autowired
  RocketMQTemplate rocketMQTemplate;

  private RateLimiter limiter = RateLimiter.create(2);

  @SneakyThrows
  @Override
  public void onMessage(Topic topic) {
    log.info(">>>>>>>>>>>>>>>start:事件:获取【{}】话题下所有高赞答案<<<<<<<<<<<<<<<<<<", topic.getTopicName());
    boolean flg = true;
    int offset = 0;
    while (flg) {
      Request get = new Builder().url(String.format(ANSWERURL, offset)).build();
      String json;
      limiter.acquire();
      try (ResponseBody responseBody = client.newCall(get)
          .execute().body()) {
        if (responseBody == null) {
          log.warn("！！！！！！！获取答案失败");
          return;
        }
        json = responseBody.string();
      } catch (Exception e) {
        log.error("！！！！！！！！获取{}话题下答案异常：{}", topic.getTopicName(), e.getMessage());
        return;
      }
      JSONObject object = JSON.parseObject(json);
      JSONObject paging = object.getJSONObject("paging");
      JSONArray data = object.getJSONArray("data");
      if (paging == null || data == null || data.isEmpty()) {
        log.warn("！！！！！！！获取答案失败");
        break;
      }

      boolean isNotEnd = !paging.getBoolean("is_end");
      boolean allMatch = data.parallelStream().allMatch(o1 -> {
        JSONObject d1 = (JSONObject) o1;
        Integer voteup_count1 = d1.getJSONObject("target").getInteger("voteup_count");
        return voteup_count1 > 10000;
      });

      flg = isNotEnd && allMatch;

      List<Answer> answers = data.parallelStream().filter(o -> {
        JSONObject d = (JSONObject) o;
        JSONObject target = d.getJSONObject("target");
        Integer voteup_count = target.getInteger("voteup_count");
        JSONObject question = target.getJSONObject("question");

        //点赞数少于一万
        if (voteup_count < 10000) {
          return false;
        }
        return question != null && question.getInteger("id") != null;
      }).map(obj -> {
        JSONObject d = (JSONObject) obj;
        JSONObject target = d.getJSONObject("target");
        JSONObject question = target.getJSONObject("question");
        JSONObject author = target.getJSONObject("author");
        Integer created_time = target.getInteger("created_time");
        Long answerId = target.getLong("id");
        Long questionId = question.getLong("id");
        return new Answer()
            .setTopicId(topic.getTopicId())
            .setAnswerId(answerId)
            .setExcerpt(target.getString("excerpt"))
            .setVoteupCount(target.getInteger("voteup_count"))
            .setQuestionId(questionId)
            .setQuestion(question.getString("title"))
            .setAuthorName(author.getString("name"))
            .setCreateDate(
                created_time == null ? null : Date.from(Instant.ofEpochSecond(created_time)))
            .setAnswerUrl(String.format(ANSWER_FEED, questionId, answerId))
            .setIsGodReplies(false);

      }).collect(Collectors.toList());
      offset += 10;
      if (!answers.isEmpty()) {
        rocketMQTemplate.convertAndSend("downloadAnswer", answers);
      }
    }
  }
}
