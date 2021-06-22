package com.zwl.json;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import com.zwl.json.fastjson.CustomSerializer;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * @author zhao_wei_long
 * @since 2021/6/18
 **/
@Data
public class Demo {

  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @Since(2.0)
  LocalDateTime newDate;

  @Until(1.0)
  Date oldDate;


  @Until(1.0)
  @JSONField(name = "accountName",alternateNames = {"nickName"})
  String name;

  @Since(2.0)
  String userName;

  @JSONField(serializeUsing = CustomSerializer.class)
  String test_str;

  @JSONField(serialize = false, deserialize = true)
  Integer qwe;
}
