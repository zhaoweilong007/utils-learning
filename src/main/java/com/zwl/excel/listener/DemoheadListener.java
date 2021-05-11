package com.zwl.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.fastjson.JSON;
import com.zwl.excel.model.DemoData;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * @author ZhaoWeiLong
 * @date 2021/5/11
 **/
@Slf4j
public class DemoheadListener extends AnalysisEventListener<DemoData> {


  @Override
  public void onException(Exception exception, AnalysisContext context) throws Exception {

    if (exception instanceof ExcelDataConvertException) {
      log.error("解析一条数据异常：{}", ExceptionUtils.getStackTrace(exception));
    }

    super.onException(exception, context);
  }

  @Override
  public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
    log.info("解析头数据：{}", JSON.toJSONString(headMap));
    super.invokeHeadMap(headMap, context);
  }

  @Override
  public void invoke(DemoData demoData, AnalysisContext analysisContext) {
    log.info("处理一条数据：{}", demoData);
  }

  @Override
  public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    log.info("全部解析完成");
  }
}
