package com.zwl.guava.collection;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ArrayTable;
import com.google.common.collect.Collections2;
import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.google.common.collect.TreeBasedTable;
import com.google.common.collect.TreeMultimap;
import com.google.common.collect.TreeMultiset;
import com.google.common.collect.TreeRangeMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableSet;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.multiset.HashMultiSet;
import org.junit.Test;

/**
 * @author ZhaoWeiLong
 * @date 2021/5/14
 **/
@Slf4j
public class CollectionTest {


  /**
   * 不可变集合
   */
  @Test
  public void imamTableTest() {
    Builder<String, Object> builder = ImmutableMap.<String, Object>builder();
    builder.put("张三", "12");
    builder.put("王五", "43");

    ImmutableMap<String, Object> immutableMap = builder.build();
    log.info("immutableMap：{}", immutableMap);

    ImmutableSet<String> immutableSet = ImmutableSet
        .<String>of("red", "orange", "green", "yellow", "blue", "purple");
    log.info("immutableSet:{}", immutableSet);

    ImmutableList<String> immutableList = ImmutableList.copyOf(immutableSet);
    log.info("immutableList:{}", immutableList);
  }


  /**
   * 允许多值的集合
   */
  @Test
  public void multTest() {
    //todo multiSet的各种实现
    HashMultiSet<String> hashMultiSet = new HashMultiSet<>();
    hashMultiSet.add("red");
    hashMultiSet.add("orange");
    hashMultiSet.add("red");
    //获取元素的计数
    System.out.println(hashMultiSet.getCount("red"));
    System.out.println(hashMultiSet.getCount("orange"));

    TreeMultiset<String> multiset = TreeMultiset.<String>create();
    multiset.add("s1");
    multiset.add("s1");
    multiset.add("s2");
    multiset.add("s3");
    //获取不重复的元素集合
    NavigableSet<String> navigableSet = multiset.elementSet();
    System.out.println(navigableSet);

    LinkedHashMultiset<String> linkedHashMultiset = LinkedHashMultiset.<String>create();
    linkedHashMultiset.add("qwe");
    linkedHashMultiset.add("ewq");
    linkedHashMultiset.add("qwe");
    linkedHashMultiset.add("wqe");

    ConcurrentHashMultiset<String> concurrentHashMultiset = ConcurrentHashMultiset.<String>create();

    //todo multiMap的各种实现

    ArrayListMultimap<String, Object> multimap = ArrayListMultimap
        .<String, Object>create();
    multimap.put("age", 12);
    multimap.put("age", 13);
    multimap.put("age", 14);
    multimap.put("name", "阿卡丽");
    multimap.put("name", "德莱文");
    multimap.put("name", "卡特琳娜");
    multimap.put("name", null);
    multimap.put(null, null);
    multimap.put(null, "test");

    for (Entry<String, Object> entry : multimap.entries()) {
      String key = entry.getKey();
      Object value = entry.getValue();
      log.info("key:{},value:{}", key, value);
    }

    System.out.println(multimap);
    System.out.println(multimap.entries());
    System.out.println(multimap.values());

    HashMultimap<String, Object> hashMultimap = HashMultimap.<String, Object>create();

    LinkedListMultimap<String, Object> linkedListMultimap = LinkedListMultimap
        .<String, Object>create();

    LinkedHashMultimap<String, Object> linkedHashMultimap = LinkedHashMultimap
        .<String, Object>create();

    TreeMultimap<String, String> treeMultimap = TreeMultimap.<String, String>create();

  }

  /**
   * 特许map，可以反转映射
   */
  @Test
  public void BiMapTest() {

    /**
     * todo 可以用 inverse()反转BiMap<K, V>的键值映射 保证值是唯一的，因此 values()返回Set而不是普通的Collection
     */

    HashBiMap<String, String> hashBiMap = HashBiMap.<String, String>create();

    ImmutableBiMap<String, String> immutableBiMap = ImmutableBiMap
        .<String, String>of("key", "value", "key2", "value2");

//    EnumBiMap<KeyEnum, ValEnum> enumBiMap = EnumBiMap
//        .<TypeEnum, TypeEnum>create(TypeEnum.class,ValEnum.class);

    System.out.println(immutableBiMap);
    ImmutableBiMap<String, String> inverse = immutableBiMap.inverse();
    System.out.println(inverse);
  }


  /**
   * 表格数据结构
   */
  @Test
  public void tableTest() {
    //todo table 通过两个键确定一个值，类似于x坐标和y坐标，excel表格类似
    //todo rowMap() 返回行的集合 row(r)返回给定行的所有列 cellSet()类似于map.entry
    //todo 主要实现

    HashBasedTable<String, String, Object> hashBasedTable = HashBasedTable
        .<String, String, Object>create();

    TreeBasedTable<String, String, Object> treeBasedTable = TreeBasedTable
        .<String, String, Object>create();

    ImmutableTable<String, String, Object> immutableTable = ImmutableTable
        .<String, String, Object>copyOf(hashBasedTable);

    ArrayTable<String, String, Object> arrayTable = ArrayTable
        .<String, String, Object>create(hashBasedTable);

    hashBasedTable.put("database1", "name", "张三");
    hashBasedTable.put("database1", "age", 23);

    hashBasedTable.put("database2", "name", "王五");
    hashBasedTable.put("database2", "age", 24);

    Map<String, Object> database1 = hashBasedTable.row("database1");
    Map<String, Object> database2 = hashBasedTable.row("database2");

    log.info("database1:{}", database1);
    log.info("database2:{}", database2);

    System.out.println(hashBasedTable.get("database1", "name"));

  }

  @Test
  public void rangeTest() {
    //todo 描述特定的区间
    TreeRangeMap<Integer, String> rangeMap = TreeRangeMap.<Integer, String>create();
    Range<Integer> closed = Range.closed(1, 10);
    rangeMap.put(closed, "foo");
    rangeMap.put(Range.open(2, 6), "bar");
    rangeMap.put(Range.open(10, 20), "qwe");
    System.out.println(rangeMap.get(2));
    System.out.println(rangeMap.get(6));
    System.out.println(rangeMap.get(10));
    System.out.println(rangeMap.get(11));
    System.out.println(rangeMap.get(20));

  }


  /**
   * 对集合的函数式处理和迭代处理
   */
  @Test
  public void functionAndIterable() {

    HashMap<Integer, String> map = Maps.<Integer, String>newHashMap();
    Random random = new Random();

    for (int i = 0; i < 100; i++) {
      map.put(random.nextInt(200), String.valueOf(random.nextInt(200)));
    }

    Map<Integer, String> filterKeys = Maps.filterKeys(map, key -> key > 100 && key < 300);
    log.info("===============过滤大于100并且小于300的key=========");
    filterKeys.entrySet().forEach(System.out::println);

    log.info("===============过滤小于50或者大于150的value=========");
    Map<Integer, String> filterValues = Maps
        .filterValues(map, val -> Integer.parseInt(val) < 50 || Integer.parseInt(val) > 150);
    filterValues.entrySet().forEach(System.out::println);

    log.info("===================转换为新的value=====================");
    Map<Integer, String> values = Maps.transformValues(filterValues, s -> "new-" + s);
    System.out.println(values);

    log.info("=========比较两个不同的Map==========");

    Map<String, String> left = Maps.newHashMap();
    left.put("a", "a1");
    left.put("b", "b1");
    left.put("c", "c1");
    left.put("d", "d1");
    left.put("e", "e");

    Map<String, String> right = Maps.newHashMap();
    right.put("a", "a2");
    right.put("b", "b2");
    right.put("c", "c2");
    right.put("d", "d2");
    right.put("e", "e");

    MapDifference<String, String> difference = Maps.difference(left, right);

    difference.entriesDiffering().forEach((Key, diff) -> {
      log.info("key:{},left value:{},right value:{}", Key, diff.leftValue(), diff.rightValue());
    });

    LinkedList<Integer> list = Lists.<Integer>newLinkedList();
    for (int i = 0; i < 20; i++) {
      list.add(random.nextInt(100));
    }
    log.info("=========================过滤大于50的list==================");
    LinkedList<Integer> linkedList = Lists.newLinkedList(Collections2.filter(list, i -> i > 50));
    linkedList.forEach(System.out::println);


    //迭代 链式调用
    ImmutableMap<Integer, String> integerStringImmutableMap = FluentIterable.<Integer>from(list)
        .filter(i -> i < 50).transform(v -> v + 100).limit(10)
        .toMap(q -> "val:" + q);




  }

}
