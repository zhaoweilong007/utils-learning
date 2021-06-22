package com.zwl.hutool;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.BetweenFormatter.Level;
import cn.hutool.core.date.ChineseDate;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.LineHandler;
import cn.hutool.core.io.file.Tailer;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.zwl.json.Person;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author zhao_wei_long
 * @since 2021/6/22
 **/
@Slf4j
public class HutoolTest {


  @Test
  public void utilsTest() {

    double a = 123456789.123456789;
    String s = Convert.digitToChinese(a);
    log.info("s:{}", s);

    Double aDouble = Convert.convert(Double.class, "123456789.999");

    log.info("double:{}", aDouble);

    DateTime dateTime = DateUtil.date();
    long time = dateTime.getTime();
    log.info("time:{}", time);
    log.info("datetime now:{}", DateUtil.now());

    DateTime parse = DateUtil.parse("2021-06-22 12:12:12");
    log.info("parse:{}", parse);

    String format = DateUtil.format(new Date(), "yyyy_MM_dd");
    log.info("format:{}", format);

    DateTime beginOfDay = DateUtil.beginOfDay(parse);
    DateTime endOfDay = DateUtil.endOfDay(parse);
    log.info("beginOfDay:{},endOfDay:{}", beginOfDay, endOfDay);

    DateTime offsetDay = DateUtil.offsetDay(new Date(), 3);
    log.info("offsetDay:{}", offsetDay);

    long between = DateUtil.between(new Date(), offsetDay, DateUnit.DAY);
    log.info("between:{}", between);

    String formatBetween = DateUtil
        .formatBetween(new Date(), DateUtil.parse("2021-10-01"), Level.DAY);
    log.info("formatBetween:{}", formatBetween);

    ChineseDate chineseDate = new ChineseDate(1998, 11, 11);
    String cyclical = chineseDate.getCyclical();
    log.info("chineseDate:{},cyclical:{}", chineseDate, cyclical);

    String zodiac = DateUtil.getZodiac(11, 11);
    String chineseZodiac = DateUtil.getChineseZodiac(1998);
    int age = DateUtil.ageOfNow("19981111");
    log.info("属：{}，星座：{},年龄：{}", chineseZodiac, zodiac, age);

    // 解析ISO时间
    LocalDateTime localDateTime = LocalDateTimeUtil.parse("2020-01-23T12:23:56");
    log.info("localDateTime:{}", localDateTime);

    // 解析自定义格式时间
    localDateTime = LocalDateTimeUtil.parse("2020-01-23", DatePattern.NORM_DATE_PATTERN);
    log.info("localDateTime:{}", localDateTime);

    String str = "";
    String str2 = null;
    boolean hasBlank = StrUtil.hasBlank(str);
    boolean hasEmpty = StrUtil.hasEmpty(str2);
    log.info("hasBlank:{},hasEmpty:{}", hasBlank, hasEmpty);

    String strformat = "我曾经跨过{}，也穿过{}，我曾经{}，转眼都{}";

    String template = StrUtil.format(strformat, "山和大海", "人山人海", "毁了我的一切", "飘散如烟");
    log.info("template:{}", template);

  }


  @Test
  public void ioTest() throws IOException {

    BufferedReader bufferedReader = ResourceUtil.getUtf8Reader("demo.txt");
    String read = IoUtil.read(bufferedReader);
    log.info("read:\n{}", read);

    String path = new ClassPathResource("").getAbsolutePath() + "/demo2.txt";
    File file = new File(path);
    if (!file.exists()) {
      System.out.println(file.createNewFile());
    }
    long copy = IoUtil
        .copy(ResourceUtil.getUtf8Reader("demo.txt"), new BufferedWriter(new FileWriter(path)));
    log.info("copy:{}", copy);

    String resource = ResourceUtil.getResource("demo.txt").getFile();
    Tailer tailer = new Tailer(FileUtil.file(resource),
        new LineHandler() {
          @Override
          public void handle(String s) {
            Console.log(s);
          }
        });

    tailer.start();


  }

  @Test
  public void test()
      throws InvocationTargetException, IllegalAccessException, IOException, InterruptedException {
    List<String> process = RuntimeUtil.execForLines("systemInfo");
    process.forEach(System.out::println);

    Method[] methods = ReflectUtil.getPublicMethods(RuntimeUtil.class);
    for (Method method : methods) {
      String methodName = method.getName();
      if (methodName.startsWith("get") && method.getParameterCount() == 0) {
        Object invoke = method.invoke(ReflectUtil.newInstance(RuntimeUtil.class), null);
        log.info("方法{}：{}", methodName, invoke);
      }
    }

 /*   Process exec = RuntimeUtil.exec("wsl");
    InputStream inputStream = exec.getInputStream();

    new Thread(() -> {
      String utf8 = IoUtil.readUtf8(inputStream);
      log.info("读取：{}", utf8);
    }).start();

    OutputStream outputStream = exec.getOutputStream();
    outputStream.write(new String("ls").getBytes(StandardCharsets.UTF_8));
    outputStream.flush();

    TimeUnit.SECONDS.sleep(10);
    outputStream.close();*/

  }

  @Test
  public void test2() {

    Dict dict = Dict.create()
        .set("name", "张三")
        .set("age", 25)
        .setIgnoreNull("profession", "杀手")
        .set("date", new Date());

    log.info("dict:{}", dict);

    String name = dict.getStr("name");
    Date date = dict.getDate("date");

    log.info("name:{},date:{}", name, date);

    Dict person = Dict.parse(new Person());

    Dict dictof = Dict.of("k1", "v1", "k2", "v2");

    log.info("person:{}", person);
    log.info("dictof:{}", dictof);

    log.info("serializer：{}", JSON.toJSONString(dict));

    // 构建node列表
    List<TreeNode<String>> nodeList = CollUtil.newArrayList();

    nodeList.add(new TreeNode<>("1", "0", "系统管理", 5));
    nodeList.add(new TreeNode<>("11", "1", "用户管理", 222222));
    nodeList.add(new TreeNode<>("111", "11", "用户添加", 0));
    nodeList.add(new TreeNode<>("2", "0", "店铺管理", 1));
    nodeList.add(new TreeNode<>("21", "2", "商品管理", 44));
    nodeList.add(new TreeNode<>("221", "2", "商品管理2", 2));

    List<Tree<String>> treeList = TreeUtil.build(nodeList, "0");

    log.info("treeList:{}", JSON.toJSONString(treeList, true));

    //配置
    TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
    // 自定义属性名 都要默认值的
    treeNodeConfig.setWeightKey("order");
    treeNodeConfig.setIdKey("rid");
    // 最大递归深度
    treeNodeConfig.setDeep(3);

    //转换器
    List<Tree<String>> treeNodes = TreeUtil.build(nodeList, "0", treeNodeConfig,
        (treeNode, tree) -> {
          tree.setId(treeNode.getId());
          tree.setParentId(treeNode.getParentId());
          tree.setWeight(treeNode.getWeight());
          tree.setName(treeNode.getName());
          // 扩展属性 ...
          tree.putExtra("extraField", 666);
          tree.putExtra("other", new Object());
        });

    log.info("=================================================================================");
    log.info("treeNodes:\n{}",JSON.toJSONString(treeList,true));
  }


}
