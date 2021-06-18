package com.zwl.json;

import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * @author zhao_wei_long
 * @since 2021/6/18
 **/
@Data
public class Demo {

  @Since(2.0)
  LocalDateTime newDate;

  @Until(1.0)
  Date oldDate;


  @Until(1.0)
  String name;

  @Since(2.0)
  String userName;


  String test_str;


  Integer qwe;
}
