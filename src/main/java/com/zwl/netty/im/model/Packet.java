package com.zwl.netty.im.model;

import lombok.Data;

/**
 * 定义数据包 数据包由五部分组成 魔数、版本号、序列化算法、指令、数据长度、数据内容
 *
 * @author ZhaoWeiLong
 * @since 2021/7/27
 **/
@Data
public abstract class Packet {


  /**
   * 版本号
   */
  private Byte version = 1;


  /**
   * @return 指令
   */
  public abstract Byte getCommand();


}
