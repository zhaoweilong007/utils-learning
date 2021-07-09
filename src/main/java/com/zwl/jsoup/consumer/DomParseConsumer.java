package com.zwl.jsoup.consumer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zwl.jsoup.model.Answer;
import com.zwl.jsoup.model.ParseDTO;
import com.zwl.jsoup.service.AnswerService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 解析dom
 *
 * @author ZhaoWeiLong
 * @since 2021/7/9
 **/
@RocketMQMessageListener(topic = "domParse", consumerGroup = "domParse-consumer")
@Slf4j
@Service
public class DomParseConsumer implements RocketMQListener<ParseDTO> {


  @Autowired
  AnswerService answerService;

  @Override
  public void onMessage(ParseDTO parseDTO) {
    String html = parseDTO.getDocument();
    Answer answer = parseDTO.getAnswer();
    log.info("==================解析DOM 【{}】：【{}】=========================", answer.getAnswerId(),
        answer.getQuestion());
    Document document = Jsoup.parse(html);
    if (document == null) {
      log.warn("！！！！！！！！！！解析dom为空！！！！！！！！！！");
      return;
    }
    Element element = document.select(".RichText.ztext.CopyrightRichText-richText").first();
    if (element == null) {
      log.warn("！！！！！！！！！！！！！！解析element为空！！！！！！！！！！！！");
      return;
    }
    List<Node> childNodes = element.childNodes();
    StringBuilder buffer = new StringBuilder();
    buffer.append("# ").append(answer.getQuestion()).append("\n\n");
    for (Node node : childNodes) {
      if (node instanceof Element) {
        Element ele = (Element) node;
        String nodeName = node.nodeName();
        if ("figure".equals(nodeName)) {
          Element img = ele.select("img").first();
          ele.replaceWith(img);
          buffer.append(img.toString());
        } else {
          buffer.append(ele);
        }
        buffer.append("\n\n");
      } else if (node instanceof TextNode) {
        TextNode textNode = (TextNode) node;
        buffer.append(textNode.text());
        buffer.append("\n\n");
      }
    }

    Answer update = new Answer()
        .setContent(buffer.toString())
        .setIsGodReplies(buffer.length() < 1500);
    boolean res = answerService.update(update,
        Wrappers.<Answer>lambdaUpdate().eq(Answer::getAnswerId, answer.getAnswerId()));
    log.info("《《《《《《《《《《《《《更新问题：{} 解析结果:{}》》》》》》》》》》》》》》", answer.getQuestion(), res);
  }
}

