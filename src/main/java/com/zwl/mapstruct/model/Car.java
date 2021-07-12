package com.zwl.mapstruct.model;

import lombok.Data;

/**
 * @author ZhaoWeiLong
 * @since 2021/7/12
 **/
@Data
public class Car {

  private String make;
  private int numberOfSeats;
  private CarType type;
}
