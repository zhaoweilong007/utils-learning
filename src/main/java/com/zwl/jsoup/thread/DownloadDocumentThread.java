package com.zwl.jsoup.thread;

import com.zwl.jsoup.model.Answer;
import com.zwl.jsoup.model.ParseDTO;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Callable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import org.jsoup.Jsoup;

/**
 * 下载document
 *
 * @author zhao_wei_long
 * @since 2021/6/23
 **/
@AllArgsConstructor
@Slf4j
public class DownloadDocumentThread implements Callable<ParseDTO> {

  private final OkHttpClient okHttpClient;
  private final Answer answer;

  @Override
  public ParseDTO call() throws IOException {
    Request request = new Builder().url(answer.getAnswerUrl()).get().build();
    String string = Objects.requireNonNull(okHttpClient.newCall(request).execute().body()).string();
     Jsoup.parse(string);
    return new ParseDTO(Jsoup.parse(string), answer);
  }

}
