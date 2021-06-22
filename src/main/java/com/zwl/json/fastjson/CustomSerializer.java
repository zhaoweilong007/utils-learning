package com.zwl.json.fastjson;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import java.io.IOException;
import java.lang.reflect.Type;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义序列化
 *
 * @author zhao_wei_long
 * @since 2021/6/22
 **/
@Slf4j
public class CustomSerializer implements ObjectSerializer {

  @Override
  public void write(JSONSerializer jsonSerializer, Object object, Object fieldName, Type type,
      int features)
      throws IOException {
    log.info("字段：{}，值：{},类型:{}",fieldName,object,type.getTypeName());
    jsonSerializer.write("zwl " + object);
  }
}
