package com.zwl.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.google.common.collect.Lists;
import com.zwl.excel.model.DemoData;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * 监听器 一条一条的读取
 *
 * @author ZhaoWeiLong
 * @date 2021/5/7
 **/
@Slf4j
public class DemoListener extends AnalysisEventListener<DemoData> {

  List<DemoData> list = Lists.newLinkedList();

  @Override
  public void invoke(DemoData demoData, AnalysisContext analysisContext) {
    log.info("处理一条数据：{}", demoData);
    list.add(demoData);
  }

  @Override
  public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    log.info("=========所有数据解析完成========");
  }
}
