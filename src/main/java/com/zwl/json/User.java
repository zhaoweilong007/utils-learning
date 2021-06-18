package com.zwl.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhao_wei_long
 * @since 2021/6/18
 **/
@Data
@Accessors(chain = true)
public class User {
  String account;
  @SerializedName(value = "userName")
  String nickName;
  @SerializedName(value = "age", alternate = {"test", "test2"})
  Integer age;

  @Expose
  String img;
}
