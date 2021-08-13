package com.zwl.protocolbuffer;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Duration;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Timestamp;
import com.google.protobuf.util.JsonFormat;
import com.zwl.protocolbuffer.AddressBook.Builder;
import com.zwl.protocolbuffer.Person.PhoneNumber;
import com.zwl.protocolbuffer.Person.PhoneType;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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


  @Test
  public void testField() {

    ProtoDemo.Builder builder = ProtoDemo.newBuilder();
    builder.setInt32(12);
    builder.setInt64(123456L);
    builder.setSint32(456);
    builder.setSint64(789465789L);
    builder.setUint32(-123);
    builder.setUint64(-789456789L);
    builder.setFloat(99.45F);
    builder.setDouble(7.6D);
    builder.setFixed32(456789);
    builder.setFixed64(78974545421L);
    builder.setSfixed32(45612);
    builder.setSfixed64(979876465456L);
    builder.setBool(true);
    builder.setStr("hello protocol buffer");
    builder.setBytes(ByteString.copyFrom("大家好啊快点哈健康和".getBytes(StandardCharsets.UTF_8)));
    builder.setName("protobuf");
    builder.setAge(18);
    builder.putMap("张三", 23);
    builder.putMap("李四", 30);
    builder.addResults(Result.newBuilder()
        .setCode(500)
        .setMsg("服务器错误")
        .setType(ResultType.fail)
        .build());
    builder.addDetails(Any.newBuilder().setValue(ByteString.copyFrom("dhajkhdjk".getBytes(
        StandardCharsets.UTF_8))));
    long second = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    builder.setTimestamp(Timestamp.newBuilder().setSeconds(second).build());
    builder.setDuration(Duration.newBuilder().setSeconds(second).build());

    ProtoDemo protoDemo = builder.build();
    log.info("protoDemo :{}", protoDemo);

    ByteString byteString = protoDemo.toByteString();

    ProtoDemo demo = null;
    try {
      demo = ProtoDemo.parseFrom(byteString.toByteArray());
      log.info("demo map :{}", demo.getMapMap());
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }

  }

}
