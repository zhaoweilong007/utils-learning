package com.zwl;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zwl.jsoup.WebCrawler;
import com.zwl.jsoup.mapper.AnswerMapper;
import com.zwl.jsoup.mapper.TopicMapper;
import com.zwl.jsoup.model.Answer;
import com.zwl.jsoup.service.AnswerService;
import com.zwl.mapstruct.mapper.CustomMapper;
import com.zwl.mapstruct.model.Custom;
import com.zwl.mapstruct.model.CustomDTO;
import com.zwl.mapstruct.model.CustomInfo;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

  @Autowired
  CustomMapper customMapper;

  @Test
  public void mappingTest() {
    Custom custom = new Custom();
    custom.setStr1("str1");
    custom.setLong1(7894564563L);
    custom.setInt1(456);
    custom.setAmount("88.89");
    custom.setStrDate("2021-07-12");
    custom.setDate(new Date());
    custom.setLocalDateTime(LocalDateTime.now());
    CustomInfo customInfo = new CustomInfo();
    customInfo.setInfo("this is cutomInfo");
    customInfo.setAddress("this is custom address");
    customInfo.setPrice(99.99);
    custom.setCustomInfo(customInfo);

    CustomDTO customDTO = customMapper.map(custom);

    log.info("customDTO:{}", customDTO);
  }

}
