package com.zwl.protocolbuffer;

import com.zwl.protocolbuffer.Person.PhoneNumber;
import com.zwl.protocolbuffer.Person.PhoneType;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhaoWeiLong
 * @since 2021/8/16
 **/
@RestController
@Slf4j
public class ProtobufWebController {

  @GetMapping(value = "/proto", produces = "application/x-protobuf")
  public AddressBook print() {
    return AddressBook.newBuilder()
        .addPerson(
            Person.newBuilder()
                .setName("张三")
                .setId(123456)
                .setEmail("374244818@qq.com")
                .addAllPhones(new ArrayList<>() {{
                  add(PhoneNumber.newBuilder()
                      .setNumber("17673165745")
                      .setType(PhoneType.mobile)
                      .build());
                }})
                .build()
        ).build();
  }

  @PostMapping(value = "/receive")
  public void receive(@RequestBody AddressBook addressBook) {
    log.info("addressBook:{}", addressBook);
  }

}
