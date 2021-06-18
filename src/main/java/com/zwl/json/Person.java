package com.zwl.json;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhao_wei_long
 * @since 2021/6/18
 **/
@Data
@Accessors(chain = true)
public class Person {
  String name;
  String phone;
  Integer age;
  Float aFloat;
  Double aDouble;
}
