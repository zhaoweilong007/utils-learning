package com.zwl.streamex;

import cn.hutool.core.util.RandomUtil;
import com.zwl.json.Person;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/5/23 13:54
 */
@Slf4j
public class ExTest {

  @Test
  public void generate() {
    Random random = new Random();
    Stream<Integer> list =
        StreamEx.generate(() -> random.nextInt(1000))
            .limit(100)
            .toListAndThen(
                integers -> {
                  return integers.stream().sorted();
                });

    String joining = StreamEx.of(list).joining(",");
    System.out.println(joining);

    List<String> combinations =
        StreamEx.ofCombinations(10, 9).map(ints -> Arrays.toString(ints)).toList();
    System.out.println(combinations);
  }

  @Test
  public void group() {
    Map<Integer, List<Person>> persons =
        StreamEx.generate(
                () -> {
                  return new Person()
                      .setName(RandomUtil.randomString(5))
                      .setAge(RandomUtil.randomInt(101));
                })
            .limit(100)
            .filter(
                person -> {
                  return person.getAge() > 0;
                })
            .groupingBy(Person::getAge);
    System.out.println(persons);
    List<Integer> ages = StreamEx.ofKeys(persons).toList();
    System.out.println(ages);
  }
}
