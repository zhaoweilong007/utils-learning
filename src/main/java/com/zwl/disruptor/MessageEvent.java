package com.zwl.disruptor;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 事件
 * @author ZhaoWeiLong
 * @since 2021/7/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageEvent<T> implements Serializable {

  private T msg;
}
