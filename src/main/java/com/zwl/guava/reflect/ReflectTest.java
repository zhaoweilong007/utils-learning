package com.zwl.guava.reflect;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 反射
 *
 * @author ZhaoWeiLong
 * @date 2021/5/31
 **/
@Slf4j
public class ReflectTest {


  @Test
  public void test() {
    //todo typeToken为了解决泛型擦除的问题

    ArrayList<String> stringList = Lists.newArrayList();
    ArrayList<Integer> intList = Lists.newArrayList();
    log.info("isAssignableFrom :{}", stringList.getClass().isAssignableFrom(intList.getClass()));

    TypeToken<ArrayList<String>> arrayListTypeToken = new TypeToken<ArrayList<String>>() {
    };
    log.info("arrayListTypeToken getRawType:{}", arrayListTypeToken.getRawType().getName());
    log.info("arrayListTypeToken getType getTypeName:{}",
        arrayListTypeToken.getType().getTypeName());

    TypeToken<String> stringTypeToken = TypeToken.of(String.class);

    Type type = stringTypeToken.getType();
    log.info("typeName:{}, className:{}", type.getTypeName(), type.getClass().getName());
    Class<? super String> rawType = stringTypeToken.getRawType();
    log.info("rawType:{}", rawType);

    //使用通配符泛型
    TypeToken<Map<?, ?>> mapTypeToken = new TypeToken<Map<?, ?>>() {

    };

    TypeToken<Map<String, Integer>> typeToken = mapToken(TypeToken.of(String.class),
        TypeToken.of(Integer.class));


  }

  static <K, V> TypeToken<Map<K, V>> mapToken(TypeToken<K> keyToken, TypeToken<V> valueToken) {
    return new TypeToken<Map<K, V>>() {
    }
        .where(new TypeParameter<K>() {
        }, keyToken)
        .where(new TypeParameter<V>() {
        }, valueToken);
  }

}
