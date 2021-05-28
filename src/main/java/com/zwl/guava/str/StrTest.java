package com.zwl.guava.str;

import com.google.common.base.CaseFormat;
import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author ZhaoWeiLong
 * @date 2021/5/27
 **/
@Slf4j
public class StrTest {


  @Test
  public void joiner() {
    Joiner joiner = Joiner.on(",").skipNulls();
    String join = joiner.join("a", "v", "q", "s");
    log.info("join:{}", join);
  }


  @Test
  public void split() {

    CharMatcher charMatcher = CharMatcher.breakingWhitespace();

    //单个字符
    List<String> strings = Splitter.on(",")
        //移除前后空白
        .trimResults()
        //忽略空字符
        .omitEmptyStrings()
        .splitToList(",foo,bar,, qux,");

    log.info("单字符：{}", strings);

    //匹配器
    List<String> qwe_asd_poi = Splitter.on(charMatcher).splitToList("qwe asd poi");
    log.info("匹配器：{}", qwe_asd_poi);

    //固定长度
    List<String> splitToList = Splitter.fixedLength(3).limit(5)
        .splitToList("qwedahjkhjkqyeghdjgahjgjhqwgej");
    log.info("固定长度:{}", splitToList
    );

    //正则
    List<String> split = Splitter.on("\\t?").splitToList("qqwe\tqeqeq\tadada\tadad");
    log.info("正则：{}", split);
  }


  @Test
  public void charMatcher() {
    //字符匹配器
    CharMatcher whitespace = CharMatcher.whitespace();
    CharMatcher javaIsoControl = CharMatcher.javaIsoControl();
    CharMatcher ascii = CharMatcher.ascii();
    //范围
    CharMatcher inRange = CharMatcher.inRange('a', 'z');
    //单字符
    CharMatcher charMatcher = CharMatcher.is('a');
    //格式转换器
    String sadad_adada = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, "sadad_adada");
    System.out.println(sadad_adada);
  }

}
