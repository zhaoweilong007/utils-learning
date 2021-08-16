package com.zwl.netty.im.model;

import io.netty.util.AttributeKey;

/**
 * @author ZhaoWeiLong
 * @since 2021/8/16
 **/
public interface Attributes {

  AttributeKey<Boolean> LOGIN = AttributeKey.valueOf("login");

}
