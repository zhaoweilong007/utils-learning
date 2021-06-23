package com.zwl.jsoup;

import java.util.Map;
import java.util.concurrent.Callable;
import org.jsoup.nodes.Document;

/**
 * @author zhao_wei_long
 * @since 2021/6/23
 **/
public class DocumentParseThread implements Callable<Map<String, Object>> {

  private Document document;

  public DocumentParseThread(Document document) {
    this.document = document;
  }

  @Override
  public Map<String, Object> call() throws Exception {

    try {
      parse();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  private void parse() {

  }
}
