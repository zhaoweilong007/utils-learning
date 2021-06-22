package com.zwl.json.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zwl.json.Demo;
import com.zwl.json.Person;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
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

  @Test
  public void feature() {

    Demo demo = new Demo();
    demo.setNewDate(LocalDateTime.now());
    demo.setOldDate(new Date());
    demo.setName("李四");
    demo.setUserName("lsisi");
    demo.setTest_str("闪现df二连");
    demo.setQwe(12);

    String json = JSON.toJSONString(demo, true);
    String jsonString = JSON.toJSONString(demo, SerializerFeature.values());
    log.info("json:{}", json);
    log.info("SerializerFeature:{}",jsonString);

    Demo d = JSON.parseObject(json, Demo.class);
    log.info("d:{}",d);

    /**
     * SerializerFeature特性如下
     *
     * QuoteFieldNames	输出key时是否使用双引号,默认为true
     * UseSingleQuotes	使用单引号而不是双引号,默认为false
     * WriteMapNullValue	是否输出值为null的字段,默认为false
     * WriteEnumUsingToString	Enum输出name()或者original,默认为false
     * UseISO8601DateFormat	Date使用ISO8601格式输出，默认为false
     * WriteNullListAsEmpty	List字段如果为null,输出为[],而非null
     * WriteNullStringAsEmpty	字符类型字段如果为null,输出为”“,而非null
     * WriteNullNumberAsZero	数值字段如果为null,输出为0,而非null
     * WriteNullBooleanAsFalse	Boolean字段如果为null,输出为false,而非null
     * SkipTransientField	如果是true，类中的Get方法对应的Field是transient，序列化时将会被忽略。默认为true
     * SortField	按字段名称排序后输出。默认为false
     * WriteTabAsSpecial	把\t做转义输出，默认为false	不推荐
     * PrettyFormat	结果是否格式化,默认为false
     * WriteClassName	序列化时写入类型信息，默认为false。反序列化是需用到
     * DisableCircularReferenceDetect	消除对同一对象循环引用的问题，默认为false
     * WriteSlashAsSpecial	对斜杠’/’进行转义
     * BrowserCompatible	将中文都会序列化为\\uXXXX格式，字节数会多一些，但是能兼容IE 6，默认为false
     * WriteDateUseDateFormat	全局修改日期格式,默认为false。JSON.DEFFAULT_DATE_FORMAT = “yyyy-MM-dd”;JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat);
     * DisableCheckSpecialChar	一个对象的字符串属性中如果有特殊字符如双引号，将会在转成json时带有反斜杠转移符。如果不需要转义，可以使用这个属性。默认为false
     * NotWriteRootClassName	含义
     * BeanToArray	将对象转为array输出
     *
     */
  }

}
