package com.zwl.jsoup;

import com.google.common.util.concurrent.FutureCallback;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jsoup.nodes.Document;

/**
 * 解析dom回调
 * @author zhao_wei_long
 * @since 2021/6/25
 **/
@Slf4j
public class ParserDocumentCallback implements FutureCallback<Document> {

  private final BlockingQueue<DocumentParseThread> parseThreads;
  private final AtomicInteger count;

  public ParserDocumentCallback(BlockingQueue<DocumentParseThread> parseThreads,
      AtomicInteger count) {
    this.parseThreads = parseThreads;
    this.count = count;
  }

  @Override
  public void onSuccess(@Nullable Document document) {
    try {
      parseThreads.put(new DocumentParseThread(document));
      count.decrementAndGet();
    } catch (InterruptedException e) {
      log.error("InterruptedException:{}", e.getMessage());
      Thread.currentThread().interrupt();
    }
  }

  @Override
  public void onFailure(Throwable throwable) {
    log.error("下载document error:{}", throwable.getMessage());
  }
}
