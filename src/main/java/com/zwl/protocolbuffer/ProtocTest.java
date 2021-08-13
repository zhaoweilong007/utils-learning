package com.zwl.protocolbuffer;

import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.zwl.protocolbuffer.AddressBook.Builder;
import com.zwl.protocolbuffer.Person.PhoneNumber;
import com.zwl.protocolbuffer.Person.PhoneType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 使用protocol buffer序列化和反序列化
 *
 * @author ZhaoWeiLong
 * @since 2021/8/13
 **/
@Slf4j
public class ProtocTest {


  @Test
  public void protocolTest() {
    //构建对象
    AddressBook addressBook = AddressBook.newBuilder()
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

    log.info("addressBook:{}", addressBook);
    ByteString byteString = addressBook.toByteString();
    //获取原始byte数组
    byte[] bytes = addressBook.toByteArray();
    log.info("addressBook byteString:{}", byteString);
    log.info("addressBook bytes:{}", Arrays.toString(bytes));
    AddressBook book = null;
    try {
      //反序列化
      book = AddressBook.parseFrom(bytes);
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }
    boolean equals = Objects.equals(book, addressBook);
    log.info("addressBook equals book:{}", equals);

    //动态消息
    Descriptor descriptor = AddressBook.getDescriptor();
    DynamicMessage dynamicMessage;
    try {
      dynamicMessage = DynamicMessage.parseFrom(descriptor, bytes);
      Map<FieldDescriptor, Object> allFields = dynamicMessage.getAllFields();
      allFields.forEach((fieldDescriptor, o) -> {
        log.info("fieldDescriptor:{},value:{}", fieldDescriptor, o);
      });
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }

    //序列化json
    try {
      String json = JsonFormat.printer().print(book);
      Builder newBuilder = AddressBook.newBuilder();
      JsonFormat.parser().merge(json, newBuilder);
      log.info("book json :{}", json);
      log.info("builder :{}", newBuilder.build());
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }


  }


}
