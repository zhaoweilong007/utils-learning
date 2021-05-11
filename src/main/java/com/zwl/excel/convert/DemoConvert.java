package com.zwl.excel.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * @author ZhaoWeiLong
 * @date 2021/5/7
 **/
public class DemoConvert implements Converter<String> {

  /**
   * 支持的java类型
   * @return
   */
  @Override
  public Class supportJavaTypeKey() {
    return String.class;
  }

  /**
   * 支持的excel类型
   * @return
   */
  @Override
  public CellDataTypeEnum supportExcelTypeKey() {
    return CellDataTypeEnum.STRING;
  }

  /**
   * 读取调用
   * 转换成java数据
   * @param cellData
   * @param excelContentProperty
   * @param globalConfiguration
   * @return
   * @throws Exception
   */
  @Override
  public String convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty,
      GlobalConfiguration globalConfiguration) throws Exception {
    return "自定义"+cellData.getStringValue();
  }

  /**
   * 写入调用
   * 转换成excel的数据
   * @param s
   * @param excelContentProperty
   * @param globalConfiguration
   * @return
   * @throws Exception
   */
  @Override
  public CellData convertToExcelData(String s, ExcelContentProperty excelContentProperty,
      GlobalConfiguration globalConfiguration) throws Exception {
    return new CellData(s);
  }
}
