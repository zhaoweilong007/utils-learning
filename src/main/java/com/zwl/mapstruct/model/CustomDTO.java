package com.zwl.mapstruct.model;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * @author ZhaoWeiLong
 * @since 2021/7/12
 **/
@Data
public class CustomDTO {


  private String str;

  private Long l1;

  private String int1;

  private Double amount;

  private String birthdate;

  private String date;

  private String localDateTime;

  private CustomInfoDTO customInfoDTO;

}
