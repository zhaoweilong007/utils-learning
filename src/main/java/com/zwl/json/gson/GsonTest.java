package com.zwl.json.gson;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.zwl.json.Demo;
import com.zwl.json.Person;
import com.zwl.json.User;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author zhao_wei_long
 * @since 2021/6/17
 **/
@Slf4j
public class GsonTest {




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





  @Test
  public void gsonBuild() {

    Gson gsonBasic = new Gson();

    Gson gson = new GsonBuilder()
        //格式化
        .setPrettyPrinting()
        //日期格式
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        //自定义序列化
//        .registerTypeAdapter(String.class,GsonSerializer.class)
        //排除策略
        .setExclusionStrategies(new ExclusionStrategy() {
          @Override
          public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            log.info("字段:{}",fieldAttributes.getName());
            return "test_str".equals(fieldAttributes.getName());
          }

          @Override
          public boolean shouldSkipClass(Class<?> aClass) {
            log.info("class type name:{}",aClass.getName());
            return aClass.isAssignableFrom(Integer.class);
          }
        })
        .setLenient()
        //指定版本
        .setVersion(2.0)
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

    log.info("to json use gsonBasic:{}",gsonBasic.toJson(demo));

    String json = gson.toJson(demo);
    log.info("tojson :{}", json);

    Map<String, Object> beanToMap = BeanUtil.beanToMap(demo);
    String mapJson = gson.toJson(beanToMap);
    log.info("map tojson:{}", mapJson);
    Demo demo2 = gson.fromJson(mapJson, Demo.class);
    log.info("demo2:{}", demo2);


  }

}
