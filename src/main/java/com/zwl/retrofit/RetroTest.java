package com.zwl.retrofit;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author ZhaoWeiLong
 * @since 2021/8/26
 **/
@Slf4j
public class RetroTest {

  @Test
  public void test() throws IOException {
    OkHttpClient client = new Builder()
        .readTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .connectionPool(new ConnectionPool(30, 10, TimeUnit.SECONDS))
        .addInterceptor(chain -> {
          log.info("拦截请求。。。。");
          Request request = chain.request().newBuilder()
              .addHeader("Accept", "application/vnd.github.v3+json").build();
          return chain.proceed(request);
        }).build();

    Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting()
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        .create();

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .callbackExecutor(Executors.newFixedThreadPool(10))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build();

    GithubService githubService = retrofit.create(GithubService.class);
    Call<List<Map<String, Object>>> listRepos = githubService.listRepos("zhaoweilong007");
    Response<List<Map<String, Object>>> execute = listRepos.execute();
    if (execute.isSuccessful()) {
      List<Map<String, Object>> body = execute.body();
      System.out.println(JSON.toJSONString(body, true));
      System.out.println("code=" + execute.code());
      System.out.println("message=" + execute.message());
    }

    //异步调用
    githubService.listRepos("square").enqueue(new Callback<>() {
      @Override
      public void onResponse(Call<List<Map<String, Object>>> call,
          Response<List<Map<String, Object>>> response) {
        log.info("请求成功");
        assert response.body() != null;
        log.info(response.body().toString());
      }

      @Override
      public void onFailure(Call<List<Map<String, Object>>> call, Throwable throwable) {
        log.error("请求失败");
      }
    });


  }

}
