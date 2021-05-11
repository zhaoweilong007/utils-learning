package com.zwl.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellExtra;
import com.zwl.excel.model.DemoExtraData;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhaoWeiLong
 * @date 2021/5/11
 **/
@Slf4j
public class DemoExtraListener extends AnalysisEventListener<DemoExtraData> {


  @Override
  public void extra(CellExtra extra, AnalysisContext context) {
    CellExtraTypeEnum type = extra.getType();
    switch (type) {
      //批注
      case COMMENT:
        log.info("读取到批注信息：{}",extra.getText());
        break;
      //超链接
      case HYPERLINK:
        log.info("读取到超链接信息：{}",extra.getText());
        break;
      //合并
      case MERGE:
        log.info("读取到合并单元格信息：firstRowIndex{},firstColumnIndex:{},lastRwoIndex:{},lastColumnIndex:{}",
            extra.getFirstRowIndex(),extra.getFirstColumnIndex(),extra.getLastRowIndex(),extra.getLastColumnIndex());
        break;
      default:
        break;
    }

    super.extra(extra, context);
  }

  @Override
  public void invoke(DemoExtraData demoExtraData, AnalysisContext analysisContext) {
    log.info("读取数据：{}", demoExtraData);
  }

  @Override
  public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    log.info("解析完成");
  }
}
