package com.zwl.json.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zwl.json.Person;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author zhao_wei_long
 * @since 2021/6/18
 **/

@Slf4j
public class JSONTest {

  @Test
  public void parseObject() {
    Person person = new Person().setName("张三").setPhone("123456").setAFloat(1.11111f)
        .setADouble(9.999999999);

    String jsonString = JSON.toJSONString(person);
    log.info("jsonString:{}", jsonString);

    String toJSONString = JSONObject.toJSONString(person);
    log.info("toJSONString:{}", toJSONString);

    String toJSONString1 = JSONArray.toJSONString(person);
    log.info("toJSONString1:{}", toJSONString1);

    JSONObject object = new JSONObject();
    object.put("name", "李四");
    object.put("phone", 123456789);
    object.put("aFloat", 123.55f);
    object.put("aDouble", 789.9999);

    Collection<Object> values = object.values();
    log.info("values:{}", values);

    Object name = object.compute("name", (s, o) -> s + "=" + o);
    log.info("compute name:{}", name);

    Person javaObject = object.toJavaObject(Person.class);
    log.info("javaObject:{}", javaObject);



  }

}
