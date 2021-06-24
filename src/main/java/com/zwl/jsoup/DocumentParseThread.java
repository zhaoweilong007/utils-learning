package com.zwl.jsoup;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * @author zhao_wei_long
 * @since 2021/6/23
 **/
public class DocumentParseThread implements Callable<Map<String, Object>> {

  private final Document document;
  private final HashMap<String, Object> map;

  public DocumentParseThread(Document document) {
    this.document = document;
    map = Maps.<String, Object>newHashMap();
  }

  @Override
  public Map<String, Object> call() {
    try {
      parse();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return map;
  }

  private void parse() {
    //解析doc
    Elements elements = document.select("");


  }
}
