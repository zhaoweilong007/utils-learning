package com.zwl;

import com.zwl.jsoup.WebCrawler;
import com.zwl.jsoup.mapper.AnswerMapper;
import com.zwl.jsoup.mapper.TopicMapper;
import com.zwl.jsoup.model.Answer;
import java.util.Date;
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

  @Test
  public void test() throws Exception {
    String cookie = "_zap=2ca50f0c-b83d-44ef-997d-8d2676fc2590; _xsrf=JY6DNS65LhUWFLk2i0C1RwPBp5FjJ3kR; ISSW=1; d_c0=\"AOCcZ9MnThOPTtspPSA9iCwSdvINf3DNPWg=|1624442445\"; captcha_session_v2=\"2|1:0|10:1624442446|18:captcha_session_v2|88:MEN5NElnSWtDQUc2SFlxaTFkMjd3ZzhHcXJ5aHM4NFZoR09tbEdXcXdsNWJMeGVqdXlBVlQ1clBWc2NyQXlrcA==|485e8281e3697eaffa46f46edcd9c44f1f6a07daddbd02cdaf2f71252afe53d4\"; __snaker__id=rEfQskRlNA2LnQhd; gdxidpyhxdE=Bwr9nH0OBpgLTeBwrkOGYrcBfwYpgjlQQuC2OYYKaP0NV1Eh9R0BEsCZiV%2FtN%2FiqzMBVUD98Bb%2BXC90tAWlZXomKfQOybSZqGScSBXTYuioMyuL38mYKL3rfyRvSA%5CiIa%2BuAYu5JK7mokhLYl0ZcJzg2NBZ%5CHWyOgiLseDbODuQohgIk%3A1624443349222; _9755xjdesxxd_=32; YD00517437729195%3AWM_TID=UnX%2Fmr40r1dBERABEBJ6lbWIUrenrxqz; captcha_ticket_v2=\"2|1:0|10:1624442456|17:captcha_ticket_v2|704:eyJ2YWxpZGF0ZSI6IkNOMzFfdVVEVVQ1Ll9LX0J1OGo0dWxhYkliSTRUQU54aFBpRUV0UldfQUJVQ0QxMUdxaWJMNEtZSnE4MU44ekl0YW9NLkRtLnQxS3dXSmVPbE0uUlNmY1NWWnVOYU1IVDB3UGZ3Q2hJLW5OR0N4ZFRRTWhxbW0wcXAuMkEucFhiaXRlNF9NU0d1UW5FTmYyS0JlMDdjVkt3S3dsemlrNUROT0lCYVVnQ201amUxVktVbURNZ0M1cWFLLW50MFNXam43MFdYaTJNNzZoRVBTQjcxVnRSeWhacWJkVkdrWmNnMlhzOUQ3emNHZUc5bWJhTm9TbnhyRGFLcnU5Q05oblRhMWdkVm1fa1lhSjZ4ekZyZDdJa3JlUUtLZUdpNE4tY3Z0Snhtd04yd18xNGRvSWEwdGs5Lmx5cEhpQ3ZXb2lIdzVoSl9lank1eU15WDR1aGZrMjVnU09ubFZQNGNDbHN4ZzBXX1EuQXk1ZzhaSzlSd0F1TS1sZk01emF1YnhHYS5valZXcm03SGtRRG0uZDVTNm8ucmFKVnVHRmp4YTJDa0NCalBLQUQubnFzMGxra0gwLTV6SjhHLThiVnFhNTZMa19vckZmSEs3N2RSWWNkc00uLlpVejFZTmNEVWg2d0RHb3NTV2h4YS42el8wckthdnoxa1phOHFlVndKZy5JMyJ9|048b80b7d451a90ee60cf0eee21988d0989695b15c80138c768d417ff4dba4e2\"; z_c0=\"2|1:0|10:1624442456|4:z_c0|92:Mi4xVG5jeEJRQUFBQUFBNEp4bjB5ZE9FeVlBQUFCZ0FsVk5XRlRBWVFDdFdRdVdFQ3RDX2E1bGx4a2lJdDgzREgxajVn|7d174d01486ed018ce953f92cf39d1b4078c20e08f34bc7389ae6c27ed16e6d9\"; tst=r; q_c1=0517f81838874dc7ae5ca1d825ddfa26|1624498155000|1624498155000; Hm_lvt_98beee57fd2ef70ccdd5ca52b9740c49=1623718869,1624323707,1624442447,1624505038; SESSIONID=Ts9lJ0FPr3Ry5dRnRU7jxhKIxHRBofa6aVKmOLmwIfw; JOID=UVAXAEOJhMeftrtPRImUGGcs9r1X1v3_-PeAAxXwvJ_niNQWICPGGf-2ukROo_Ik_lv2y7nCJ7yda4d4lDkqSlc=; osd=UlwWB0OKiMaYtrhDRY6UG2st8b1U2vz4-PSMAhLwv5Pmj9QVLCLBGfy6u0NOoP4l-Vv1x7jFJ7-RaoB4lzUrTVc=; Hm_lpvt_98beee57fd2ef70ccdd5ca52b9740c49=1624609583; KLBRSID=2177cbf908056c6654e972f5ddc96dc2|1624609596|1624609546";
    webCrawler.start("");

  }

  @Test
  public void testDB() {

    Answer answer = new Answer();
    answer.setTopicId(null);
    answer.setAnswerId(null);
    answer.setQuestionId(null);
    answer.setQuestion("qweqweqweqweq");
    answer.setVoteupCount(123124);
    answer.setExcerpt("adafadadjhkhjkad");
    answer.setAuthorName("qweqwe");
    answer.setCreateDate(new Date());

    System.out.println(answerMapper.insert(answer));

  }
}
