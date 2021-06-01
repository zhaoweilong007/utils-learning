package com.zwl.guava.concurrent;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Test;

/**
 * 并发
 *
 * @author ZhaoWeiLong
 * @date 2021/6/1
 **/
public class ConcurrentTest {

  @Test
  public void test() throws ExecutionException, InterruptedException {
    // todo 先来看下jdk提供的futureTask，实现RunnableFuture，继承了Future、Runnable接口
    FutureTask<String> futureTask = new FutureTask<String>(new Callable<String>() {
      @Override
      public String call() throws Exception {
        return "测试异步任务";
      }
    });
    futureTask.run();
    String res = futureTask.get();
    System.out.println(res);
  }


  @Test
  public void guavaTest() throws ExecutionException, InterruptedException {
    //执行线程服务对象
    ListeningExecutorService listeningExecutorService = MoreExecutors
        .listeningDecorator(Executors.newFixedThreadPool(10));
    //提交任务
    ListenableFuture<String> callableCall = listeningExecutorService.submit(() -> {
      System.out.println("callable call");
      return "res";
    });
    //添加回调
    Futures.addCallback(callableCall, new FutureCallback<String>() {
      @Override
      public void onSuccess(@Nullable String s) {
        //成功的回调
        System.out.println("success:" + s);
      }

      @Override
      public void onFailure(Throwable throwable) {
        //失败的回调
        System.out.println("failure");
      }
    }, listeningExecutorService);

    //任务接口转换,异步转换
    ListenableFuture<String> transformAsync = Futures
        .transformAsync(callableCall, new AsyncFunction<String, String>() {
          @Override
          public ListenableFuture<String> apply(@Nullable String s) throws Exception {
            return Futures.immediateFuture(s.toUpperCase() + " async");
          }
        }, MoreExecutors.directExecutor());

    String res = transformAsync.get();

    System.out.println(res);

    //多线程并发去结果集合

    ArrayList<ListenableFuture<String>> arrayList = Lists
        .<ListenableFuture<String>>newArrayList();
    for (int i = 0; i < 10; i++) {
      int finalI = i;
      ListenableFuture<String> listenableFuture = listeningExecutorService
          .submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
              return String.valueOf(finalI);
            }
          });
      arrayList.add(listenableFuture);
    }

    ListenableFuture<List<String>> allAsList = Futures.allAsList(arrayList);
    List<String> list = allAsList.get();
    System.out.println(list);


  }

}
