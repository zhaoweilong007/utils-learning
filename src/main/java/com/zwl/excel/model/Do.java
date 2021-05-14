package com.zwl.excel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author ZhaoWeiLong
 * @date 2021/5/11
 **/
@Data
@AllArgsConstructor
public class Do {

  @ExcelProperty("姓名")
  private String name;

  @ExcelProperty("年龄")
  private Integer age;

  @ExcelProperty("性别")
  private String sex;

  @ExcelProperty("职业")
  private String position;

}
