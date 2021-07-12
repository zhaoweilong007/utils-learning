package com.zwl.mapstruct;

import com.zwl.mapstruct.mapper.CarMapper;
import com.zwl.mapstruct.model.Car;
import com.zwl.mapstruct.model.CarDTO;
import com.zwl.mapstruct.model.CarType;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhaoWeiLong
 * @since 2021/7/12
 **/
@Slf4j
public class MapperTest extends TestCase {

  public void test1() {
    Car car = new Car();
    car.setMake("morris");
    car.setNumberOfSeats(15);
    car.setType(CarType.bus);

    CarDTO carDTO = CarMapper.INSTANCE.carToCarDto(car);
    log.info("carDTO:{}", carDTO);

  }



}
