package com.zwl.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhaoWeiLong
 * @date 2021/5/11
 **/
@Slf4j
public class NoModelListener extends AnalysisEventListener<Map<Integer, String>> {

  @Override
  public void invoke(Map<Integer, String> integerStringMap, AnalysisContext analysisContext) {
    log.info("处理数据：{}",integerStringMap);
  }

  @Override
  public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    log.info("解析完成");
  }
}
