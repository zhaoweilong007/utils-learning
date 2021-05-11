package com.zwl.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.zwl.excel.model.ConvertData;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhaoWeiLong
 * @date 2021/5/11
 **/
@Slf4j
public class ConvertListener extends AnalysisEventListener<ConvertData> {

  @Override
  public void invoke(ConvertData convertData, AnalysisContext analysisContext) {
    //一条一条处理数据
    log.info("读取数据：{}",convertData);
  }

  @Override
  public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    //全部解析完后调用
    log.info("数据解析完成");
  }
}
