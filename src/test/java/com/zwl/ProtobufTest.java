package com.zwl;

import cn.hutool.core.lang.Console;
import com.zwl.protocolbuffer.AddressBook;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 * @author ZhaoWeiLong
 * @since 2021/8/16
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProtobufTest {

  @Autowired
  RestTemplate restTemplate;

  @Test
  public void test() throws URISyntaxException {

    AddressBook addressBook = restTemplate
        .getForObject("http://localhost:8888/proto", AddressBook.class);
    Console.log("response :{}", addressBook);

    RequestEntity<AddressBook> httpEntity = new RequestEntity<AddressBook>(addressBook,
        new HttpHeaders() {{
          add("Content-Type", "application/x-protobuf");
        }},
        HttpMethod.POST,
        new URI("http://localhost:8888/receive"));

    HttpStatus statusCode = restTemplate
        .exchange("http://localhost:8888/receive", HttpMethod.POST, httpEntity, Void.class)
        .getStatusCode();
    Console.log("statusCode:{}", statusCode);
  }

}
