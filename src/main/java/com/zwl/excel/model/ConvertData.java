package com.zwl.excel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.zwl.excel.convert.DemoConvert;
import lombok.Data;

/**
 * @author ZhaoWeiLong
 * @date 2021/5/7
 **/
@Data
public class ConvertData {



  @ExcelProperty(converter = DemoConvert.class)
  private String str1;

  private String str2;


  //百分比格式化有问题不清楚什么原因 最终时这样的1210%
  @NumberFormat("#.##%")
  @ExcelProperty(index = 4)
  private String cost;

  @NumberFormat("#岁")
  @ExcelProperty(index = 3)
  private String age;

  @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
  @ExcelProperty(index = 2)
  private String date;

}
