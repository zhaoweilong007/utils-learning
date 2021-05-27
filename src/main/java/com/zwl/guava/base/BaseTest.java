package com.zwl.guava.base;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

/**
 * @author ZhaoWeiLong
 * @date 2021/5/14
 **/
public class BaseTest {

  @Test
  public void optionalTest() {
    String str = null;
    String str1 = "qweqweqweq";

    //构建一个不为空的optional对象
    Optional<String> nullable = Optional.of(str1);

    //构建一个可能为空的optional
    Optional<String> nullable1 = Optional.ofNullable(str);

    nullable.ifPresent(System.out::println);
    String s1 = nullable.map(s -> s.toUpperCase(Locale.ROOT)).map(s -> s + " test").get();
    System.out.println(s1);

    //为空时
    System.out.println(nullable1.orElseGet(() -> "other"));

    //判断是否为空
    System.out.println(nullable1.isPresent());
    System.out.println(nullable.isPresent());
  }


  @Test
  public void preconditionTest() {

    Predicate<String> predicate = s -> s.startsWith("qwer");

    //检查参数
    Preconditions.checkArgument(predicate.test("qwewqeq"), "arg is not true");

    String str = "qweqweqwe";
    //检查是否为空
    Preconditions.checkNotNull(str, "arg is null");
    //检查状态
    Preconditions.checkState(predicate.test("qpqowoe"));

  }

  @Test
  public void sortTest() {
    Person person = new Person();
    person.setFistName("张");
    person.setLastName("三");
    person.setAge(21);
    Person person2 = new Person();
    person2.setFistName("李");
    person2.setLastName("四");
    person2.setAge(25);

    Person person3 = new Person();
    person3.setFistName("王");
    person3.setLastName("五");
    person3.setAge(23);

    System.out.println(person.compareTo(person2));
    System.out.println(person.compareTo(person3));

    ArrayList<Person> people = Lists.newArrayList(person, person2, person3);

    //使用Ordering 链式排序器

    Ordering<Person> ordering = Ordering
        //自然排序
        .natural()
        //null放在最前面
        .nullsFirst()
        //按照结果值排序
        .onResultOf(new Function<Person, String>() {
          @Override
          public String apply(Person foo) {
            return foo.lastName;
          }
        });

    List<Person> people1 = ordering.sortedCopy(people);
    System.out.println(people1);
  }


  @Test
  public void exceptionTest() throws IOException {
    try {
      preconditionTest();
    } catch (Exception e) {
      Throwables.propagateIfPossible(e, IllegalArgumentException.class);
      Throwables.propagateIfPossible(e, NullPointerException.class);
      throw e;
    }
  }


  @Data
  class Person implements Comparable<Person> {

    private String fistName;
    private String lastName;
    private Integer age;


    @Override
    public int compareTo(@NotNull Person o) {

      return ComparisonChain.start().
          compare(this.fistName, o.fistName)
          .compare(this.lastName, o.lastName)
          .compare(this.age, o.age).result();
    }
  }

}
