package com.zwl.guava.function;

import com.google.common.base.Functions;
import com.google.common.base.Supplier;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Test;

/**
 * @author ZhaoWeiLong
 * @date 2021/5/27
 **/
@Slf4j
public class FunctionTest {

  @FunctionalInterface
  public interface Handler<T, S> {

    S handler(T t);

  }

  @Test
  public void test() {
    //自定义函数
    Handler<String, String> handler = o -> {
      log.info("执行处理：{}", o);
      return o;
    };

    //jdk function
    Function<String, String> function = o -> {
      log.info("执行处理：{}", o);
      return o;
    };

    //消费者
    Consumer<String> consumer = s -> {
      System.out.println(s.toUpperCase());
    };

    //提供者
    Supplier<String> supplier = () -> {
      return "qwer";
    };

    com.google.common.base.Function<String, String> funa = new com.google.common.base.Function<String, String>() {
      @Override
      public @Nullable String apply(@Nullable String s) {

        return s.toUpperCase(Locale.ROOT);
      }

    };

    com.google.common.base.Function<String, String> funb = new com.google.common.base.Function<String, String>() {

      @Override
      public @Nullable String apply(@Nullable String s) {
        return s + " 你好";
      }

    };

    //组合函数
    com.google.common.base.Function<String, String> composeFunc = Functions.compose(funa, funb);

    System.out.println(composeFunc.apply("xiao ming"));

    LinkedHashMap<String, String> linkedHashMap = Maps
        .<String, String>newLinkedHashMap();

    linkedHashMap.put("阿卡丽", "20");
    linkedHashMap.put("卡特琳娜", "21");
    linkedHashMap.put("疾风剑豪", "22");

    com.google.common.base.Function<String, String> mapFunction = Functions
        .forMap(linkedHashMap);

    String str = mapFunction.apply("阿卡丽");
    System.out.println(str);

    ArrayList<String> list = Lists.<String>newArrayList("阿卡丽", "卡特琳娜");
    Collection<String> transform = Collections2.transform(list, mapFunction);
    transform.forEach(System.out::println);

    com.google.common.base.Function<Object, String> forSupplier = Functions
        .forSupplier(supplier);

    System.out.println(forSupplier.apply("张三"));


  }

}
