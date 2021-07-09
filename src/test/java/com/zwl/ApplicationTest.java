package com.zwl;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zwl.jsoup.WebCrawler;
import com.zwl.jsoup.mapper.AnswerMapper;
import com.zwl.jsoup.mapper.TopicMapper;
import com.zwl.jsoup.model.Answer;
import com.zwl.jsoup.service.AnswerService;
import java.io.InputStream;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhao_wei_long
 * @since 2021/6/25
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplicationTest {

  @Autowired
  @Qualifier("topicMapper")
  TopicMapper topicMapper;

  @Qualifier("answerMapper")
  @Autowired
  AnswerMapper answerMapper;

  @Autowired
  WebCrawler webCrawler;

  @Autowired
  AnswerService answerService;


  @Test
  public void insertBatch() {
    InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("answer.json");
    assert inputStream != null;
    String json = IoUtil.readUtf8(inputStream);
    List<Answer> answers = JSON.parseObject(json, new TypeReference<List<Answer>>() {
    }.getType());
    answers.forEach(System.out::println);

    boolean save = answerService.saveBatch(answers);
  }

}
