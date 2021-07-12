package com.zwl.mapstruct.model;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZhaoWeiLong
 * @since 2021/7/12
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Custom {

  private String str1;

  private Long long1;

  private Integer int1;

  private String amount;

  private String strDate;

  private Date date;

  private LocalDateTime localDateTime;

  private CustomInfo customInfo;

}
