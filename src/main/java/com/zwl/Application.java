package com.zwl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

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

}
