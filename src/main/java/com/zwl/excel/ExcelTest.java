package com.zwl.excel;

import cn.hutool.core.io.resource.ClassPathResource;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.google.common.collect.Lists;
import com.zwl.excel.listener.ConvertListener;
import com.zwl.excel.listener.DemoExtraListener;
import com.zwl.excel.listener.DemoListener;
import com.zwl.excel.listener.DemoheadListener;
import com.zwl.excel.listener.NoModelListener;
import com.zwl.excel.model.ConvertData;
import com.zwl.excel.model.DemoData;
import com.zwl.excel.model.DemoExtraData;
import com.zwl.excel.model.Do;
import com.zwl.excel.model.FillData;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
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


  /**
   * 同步读取
   */
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

    EasyExcel.read(inputStream, new NoModelListener()).sheet().doRead();
  }


  @Test
  public void excelWrite() throws FileNotFoundException {
    //write test

    LinkedList<Do> list = Lists.newLinkedList();
    for (int i = 0; i < 10; i++) {
      list.add(new Do("张三" + i, 20 + i, "男", "开发"));
    }
    String filePath = new ClassPathResource("excel").getAbsolutePath() + "/write.xlsx";
    FileOutputStream outputStream = new FileOutputStream(filePath);

    EasyExcel.write(outputStream, Do.class).build().write(list, new WriteSheet()).finish();
  }


  /**
   * excel填充
   */
  @Test
  public void excelFill() {
    String fileName = this.getClass().getResource("/").getPath() + "/simpleFill.xlsx";
    String tmpPath = classLoader.getResource("excel/simple.xlsx").getPath();

    FillData fillData = new FillData();
    fillData.setName("张三");
    fillData.setNumber(29);

    EasyExcel.write(fileName).withTemplate(tmpPath).sheet().doFill(fillData);
  }


  @Test
  public void excelWriteHead() {

    String filePath = new ClassPathResource("excel").getAbsolutePath() + "/test.xlsx";

    ExcelWriterBuilder writerBuilder = EasyExcel.write();
// 输出的文件对象，可以是File、路径（字符串）或者OutputStream实例
    writerBuilder.file(filePath);
// 指定sheet，可以是数字序号sheetNo或者字符串sheetName，可以不设置，由下面提到的WriteSheet覆盖
    writerBuilder.sheet("demo");
// 文件的密码
// Excel文件格式，包括ExcelTypeEnum.XLSX和ExcelTypeEnum.XLS
    writerBuilder.excelType(ExcelTypeEnum.XLSX);
// 是否自动关闭输出流
    writerBuilder.autoCloseStream(true);
// 指定文件的标题行，可以是Class对象（结合@ExcelProperty注解使用），或者List<List<String>>实例
    writerBuilder.head(Collections.singletonList(Collections.singletonList("head")));
// 构建ExcelWriter实例
    ExcelWriter excelWriter = writerBuilder.build();
    List<List<String>> data = new ArrayList<>();
    ArrayList<String> objectArrayList = new ArrayList<>();
    objectArrayList.add("sadadada");
    data.add(objectArrayList);
// 构建输出的sheet
    WriteSheet writeSheet = new WriteSheet();
    writeSheet.setSheetName("target");
    excelWriter.write(data, writeSheet);
// 这一步一定要调用，否则输出的文件有可能不完整
    excelWriter.finish();
  }
}
