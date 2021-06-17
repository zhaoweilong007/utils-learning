package com.zwl.json.json;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author zhao_wei_long
 * @since 2021/6/17
 **/
@Slf4j
public class GsonTest {


  @Data
  @Accessors(chain = true)
  private class Person {

    String name;
    String phone;
    Integer age;
    Float aFloat;
    Double aDouble;

  }


  @Test
  public void parseObject() {
    Person person = new Person().setName("张三").setPhone("123456").setAFloat(1.11111f)
        .setADouble(9.999999999);

    Gson gson = new Gson();
    String json = gson.toJson(person);
    log.info("tojson:{}", json);

    log.info("tojson tree:{}", gson.toJsonTree(person));

    Person p = gson.fromJson(json, Person.class);
    JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
    String name = jsonObject.get("name").getAsString();
    Integer age = jsonObject.get("age").getAsInt();
    log.info("fromJson to Person :{}", p);
    log.info("fromjson to JsonObject :{}", jsonObject);
  }


  @Test
  public void parseArray() {
    AtomicInteger atomicInteger = new AtomicInteger(1);
    List<Person> list = Stream.generate(
        () -> new Person().setName("李四" + atomicInteger.getAndIncrement()).setPhone("123456")
            .setAFloat(12.6666f).setADouble(6.66666)).limit(10).collect(Collectors.toList());

    Gson gson = new Gson();
    List<Person> personList = gson.fromJson(gson.toJson(list), new TypeToken<List<Person>>() {
    }.getType());
    List<Map<String, Object>> maps = gson
        .fromJson(gson.toJson(list), new TypeToken<List<Map<String, Object>>>() {
        }.getType());

    personList.forEach(System.out::println);
    log.info("=====================");
    maps.forEach(System.out::println);
  }

  @Data
  @Accessors(chain = true)
  private class User {

    String account;
    @SerializedName(value = "userName")
    String nickName;
    @SerializedName(value = "age", alternate = {"test", "test2"})
    Integer age;

    @Expose
    String img;
  }

  @Test
  public void toCustomType() {
    Gson gson = new Gson();
    User user = new User().setAccount("admin").setNickName("阿斯达克").setAge(12)
        .setImg("http://qwe.com");

    String json = gson.toJson(user);
    log.info("user json :{}", json);
    log.info("================================");
    HashMap<String, Object> map =
        Maps.<String, Object>newHashMap();
    map.put("account", "admin");
    map.put("userName", "阿斯达克");
    map.put("test", 13);
    map.put("img", "http://qwe.com");

    User user2 = gson.fromJson(gson.toJson(map), User.class);
    log.info("user2:{}", user2);
    //User(account=admin, nickName=阿斯达克, age=13, img=http://qwe.com)

    Gson gson_expose = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    String toJson = gson_expose.toJson(user);
    log.info("expose json:{}", toJson);
    User user_expose = gson_expose.fromJson(json, User.class);
    log.info("user_expose:{}", user_expose);
  }


  @Data
  class Demo {

    @Since(2)
    LocalDateTime newDate;

    @Since(1)
    Date oldDate;


    @Since(1)
    String name;

    @Since(2)
    String userName;


    String test_str;


    Integer qwe;

  }


  @Test
  public void gsonBuild() {

    Gson gson = new GsonBuilder()
        //格式化
        .setPrettyPrinting()
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        //排除策略
        .setExclusionStrategies(new ExclusionStrategy() {
          @Override
          public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            return "test_str".equals(fieldAttributes.getName());
          }

          @Override
          public boolean shouldSkipClass(Class<?> aClass) {

            return aClass.isAssignableFrom(Integer.class);
          }
        })
        .setLenient()
        //指定版本
        .setVersion(2)
        //字段命名策略
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        //字段命名策略
        .setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();

    Demo demo = new Demo();
    demo.setNewDate(LocalDateTime.now());
    demo.setOldDate(new Date());
    demo.setName("张三");
    demo.setUserName("张三");
    demo.setTest_str("test");
    demo.setQwe(123);

    String json = gson.toJson(demo);
    log.info("tojson :{}", json);

    Map<String, Object> beanToMap = BeanUtil.beanToMap(demo);
    String mapJson = gson.toJson(beanToMap);
    log.info("map tojson:{}", mapJson);
    Demo demo2 = gson.fromJson(mapJson, Demo.class);
    log.info("demo2:{}", demo2);


  }

}
