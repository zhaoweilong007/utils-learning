package com.zwl;

import com.google.common.collect.Lists;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

/**
 * @author zhao_wei_long
 * @since 2021/6/25
 **/
@SpringBootApplication
@EnableAsync
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }


  /**
   * 配置protobuf消息转换器
   *
   * @return
   */
  @Bean
  public ProtobufHttpMessageConverter protobufHttpMessageConverter() {
    return new ProtobufHttpMessageConverter();
  }

  /**
   * restTemplate 消息转换
   * @param protobufHttpMessageConverter
   * @return
   */
  @Bean
  public RestTemplate restTemplate(
      ProtobufHttpMessageConverter protobufHttpMessageConverter) {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setMessageConverters(Lists.newArrayList(protobufHttpMessageConverter));
    return restTemplate;
  }
}
