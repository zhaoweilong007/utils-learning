package com.zwl.jsoup.thread;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Callable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 下载document
 *
 * @author zhao_wei_long
 * @since 2021/6/23
 **/
@AllArgsConstructor
@Slf4j
public class DownloadDocumentThread implements Callable<Document> {

  private final OkHttpClient okHttpClient;
  private final String url;

  @Override
  public Document call() throws IOException {
    Request request = new Builder().url(url).get().build();
    String string = Objects.requireNonNull(okHttpClient.newCall(request).execute().body()).string();
    return Jsoup.parse(string);
  }

}
