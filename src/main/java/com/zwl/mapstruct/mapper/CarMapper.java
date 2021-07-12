package com.zwl.mapstruct.mapper;

import com.zwl.mapstruct.model.Car;
import com.zwl.mapstruct.model.CarDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author ZhaoWeiLong
 * @since 2021/7/12
 **/
@Mapper
public interface CarMapper {

  CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

  @Mapping(source = "numberOfSeats", target = "seatCount")
  CarDTO carToCarDto(Car car);
}
