package com.zwl.jsoup.consumer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.RateLimiter;
import com.zwl.jsoup.mapper.AnswerMapper;
import com.zwl.jsoup.model.Answer;
import com.zwl.jsoup.model.ParseDTO;
import com.zwl.jsoup.service.AnswerService;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.ResponseBody;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 解析topic
 *
 * @author ZhaoWeiLong
 * @since 2021/7/9
 **/
@RocketMQMessageListener(topic = "downloadAnswer", consumerGroup = "downloadAnswer-consumer")
@Slf4j
@Service
public class DownloadAnswerConsumer implements RocketMQListener<List<Answer>> {


  @Autowired
  AnswerMapper answerMapper;

  @Autowired
  AnswerService answerService;

  @Autowired
  OkHttpClient client;

  @Autowired
  ListeningExecutorService listeningExecutorService;

  @Autowired
  RocketMQTemplate rocketMQTemplate;

  @Value("${zhihu.cookie}")
  private String cookie;


  private RateLimiter limiter = RateLimiter.create(2);


  @Override
  public void onMessage(List<Answer> answerList) {
    log.info(">>>>>>>>>>>>>>>start:事件：下载高赞回答>>>>>>>>>>>>>>>>>");
    boolean insertBatch;
    try {
      List<Answer> answers = answerList.stream().filter(answer -> {
        Integer count = answerMapper
            .selectCount(
                Wrappers.<Answer>lambdaQuery().eq(Answer::getAnswerId, answer.getAnswerId()));
        return count == 0;
      }).collect(Collectors.toList());
      if (!answers.isEmpty()) {
        insertBatch = answerService.saveBatch(answerList);
        log.info("批量插入回答res：{}", insertBatch);
      }
      answerList.forEach(answer -> {
        Request request = new Builder().url(answer.getAnswerUrl()).get().
            addHeader("cookie", cookie).
            build();
        String json;
        ResponseBody responseBody = null;
        try {
          limiter.acquire();
          responseBody = client.newCall(request).execute().body();
          if (responseBody == null) {
            return;
          }
          json = responseBody.string();
          ParseDTO parseDTO = new ParseDTO(json, answer);
          rocketMQTemplate.convertAndSend("domParse", parseDTO);
        } catch (IOException e) {
          log.warn("下载【{}】回答失败：{}", answer.getQuestion(), e.getMessage());
        } finally {
          if (responseBody != null) {
            responseBody.close();
          }
        }
      });
    } catch (Exception e) {
      log.error("回答批量插入失败：{}", e.getMessage());
    }
  }
}
