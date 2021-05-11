package com.zwl.excel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import java.util.Date;
import lombok.Data;

/**
 * 值得注意的是 多个属性时属性的excel表的顺序一一对应
 *
 * @author ZhaoWeiLong
 * @date 2021/5/7
 **/
@Data
public class DemoData {

  //不建议同时使用index和name，有些属性测试值为空

  @ExcelProperty(index = 0)
  private String str1;

  @ExcelProperty("字符串标题2")
  private String str2;

  @ExcelProperty(index = 4)
  private Double cost;

  private Integer age;

  private Date date;


}
