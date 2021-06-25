package com.zwl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhao_wei_long
 * @since 2021/6/25
 **/
@SpringBootApplication
@MapperScan(basePackages = "com.zwl")
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
