package com.zwl.excel;

import cn.hutool.core.io.resource.ClassPathResource;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.zwl.excel.listener.ConvertListener;
import com.zwl.excel.listener.DemoExtraListener;
import com.zwl.excel.listener.DemoListener;
import com.zwl.excel.listener.DemoheadListener;
import com.zwl.excel.listener.NoModelListener;
import com.zwl.excel.model.ConvertData;
import com.zwl.excel.model.DemoData;
import com.zwl.excel.model.DemoExtraData;
import com.zwl.excel.model.Do;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

/**
 * excel 工具类使用
 *
 * @author ZhaoWeiLong
 * @date 2021/5/7
 **/
@Slf4j
public class ExcelTest {

  ClassLoader classLoader;

  @Before
  public void before() {
    classLoader = this.getClass().getClassLoader();
  }

  @Test
  public void excelReader() {
    InputStream inputStream = new ClassPathResource("excel/demo2.xlsx").getStream();
    //第一种
    EasyExcel.read(inputStream, DemoData.class, new DemoListener()).sheet(0)
        .doRead();

    //第二种
    String path = this.getClass().getClassLoader().getResource("excel/demo2.xlsx").getPath();

    ExcelReader excelReader = null;
    try {
      excelReader = EasyExcel.read(path, DemoData.class, new DemoListener()).build();
      ReadSheet readSheet = EasyExcel.readSheet(0).build();
      excelReader.read(readSheet);
    } finally {
      if (excelReader != null) {
        excelReader.finish();
      }
    }

    //每个sheet使用不同的监听器
    ReadSheet sheet1 = EasyExcel.readSheet(0).head(DemoData.class)
        .registerReadListener(new DemoListener()).build();
    ReadSheet sheet2 = EasyExcel.readSheet(1).head(DemoData.class)
        .registerReadListener(new DemoheadListener()).build();

    EasyExcel.read(path).build().read(sheet1, sheet2);

  }


  /**
   * 自定义转换器
   */
  @Test
  public void convertData() {
    InputStream inputStream = classLoader.getResourceAsStream("excel/demo2.xlsx");
    EasyExcel.read(inputStream, ConvertData.class, new ConvertListener()).
        sheet(0).doRead();
  }


  /**
   * 测试有问题 读取额外的信息，超链接、批注
   */
  @Test
  public void extraRead() {
    InputStream inputStream = classLoader.getResourceAsStream("excel/extra.xlsx");
    EasyExcel.read(inputStream, DemoExtraData.class, new DemoExtraListener()).
        extraRead(CellExtraTypeEnum.COMMENT).
        extraRead(CellExtraTypeEnum.HYPERLINK).extraRead(CellExtraTypeEnum.MERGE).
        sheet().doRead();
  }


  @Test
  public void synchronousRead() {
    String inputStream = Objects.requireNonNull(classLoader.getResource("excel/demo.xlsx"))
        .getPath();
    List<Do> list = EasyExcel.read(inputStream).head(Do.class).sheet().doReadSync();
    list.forEach(System.out::println);
    List<Map<Integer, String>> sync = EasyExcel.read(inputStream).sheet().doReadSync();
    for (Map<Integer, String> map : sync) {
      log.info("读取到数据：{}", map);
    }

    EasyExcel.read(inputStream,new NoModelListener()).sheet().doRead();
  }


  @Test
  public void convertExcel() {

  }


  @Test
  public void excelWrite() {

  }

}
