package com.zwl.json.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/**
 * @author zhao_wei_long
 * @since 2021/6/22
 **/
public class GsonSerializer implements JsonSerializer<String> {


  @Override
  public JsonElement serialize(String o, Type type,
      JsonSerializationContext jsonSerializationContext) {
    return new JsonPrimitive("zwl" + o);
  }
}
