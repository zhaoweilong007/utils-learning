package com.zwl.mapstruct.mapper;

import com.zwl.mapstruct.model.Custom;
import com.zwl.mapstruct.model.CustomDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @author ZhaoWeiLong
 * @since 2021/7/12
 **/
@Mapper(componentModel = "spring")
public interface CustomMapper {

  @Mappings(
      {
          @Mapping(source = "custom.str1", target = "str"),
          @Mapping(source = "custom.long1", target = "l1"),
          @Mapping(source = "custom.amount", target = "amount", numberFormat = "#.##"),
          @Mapping(source = "custom.strDate", target = "birthdate", dateFormat = "yyyy-MM-dd"),
          @Mapping(source = "custom.date", target = "date", dateFormat = "yyyy年MM月dd日"),
          @Mapping(source = "custom.localDateTime", target = "localDateTime", dateFormat = "yyyy.MM.dd HH:mm:ss"),
          @Mapping(source = "custom.customInfo.info", target = "customInfoDTO.content"),
          @Mapping(source = "custom.customInfo.price", target = "customInfoDTO.price", numberFormat = "#.##"),
      }
  )
  CustomDTO map(Custom custom);

}
