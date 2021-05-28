package com.zwl.http.okhttp;

import cn.hutool.core.io.IoUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

/**
 * @author ZhaoWeiLong
 * @date 2021/5/28
 **/
public class OkHttpTest {

  private final File cacheFile = new File(this.getClass().getResource("").getPath());
  private final int cacheSize = 10 * 1024 * 1024;

  private final OkHttpClient client = new OkHttpClient.Builder().
      connectTimeout(5, TimeUnit.SECONDS)
      .readTimeout(5, TimeUnit.SECONDS)
      .writeTimeout(5, TimeUnit.SECONDS)
      .callTimeout(5, TimeUnit.SECONDS)
      .addInterceptor(chain -> {
        System.out.println("===================执行拦截器方法================");
        Request request = chain.request();
        return chain.proceed(request);
      }).
          cache(new Cache(cacheFile, cacheSize)).
          build();

  public static final String url = "";


  /**
   * 同步get
   *
   * @return
   */
  public String get() {
    Request get = new Builder().url(url).get().addHeader("access_token", "qweqeq")
        .build();
    try (Response response = client.newCall(get).execute()) {
      if (!response.isSuccessful()) {
        throw new IOException("UnExpected code" + response.code());
      }
      return Objects.requireNonNull(response.body()).string();
    } catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }

  /**
   * 异步get
   */
  public void asyncGet() {
    Request get = new Builder().url(url).get().build();
    client.newCall(get).enqueue(new Callback() {
      @Override
      public void onFailure(@NotNull Call call, @NotNull IOException e) {
        e.printStackTrace();
      }

      @Override
      public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        if (!response.isSuccessful()) {
          throw new IOException("exception message：" + response.message());
        }
        System.out.println(response.body());
      }
    });

  }

  /**
   * post发送Json
   *
   * @param json 参数
   * @return
   */
  public String postString(String json) {
    MediaType mediaType = MediaType.get("application/json; charset=utf-8");
    RequestBody requestBody = RequestBody.create(json, mediaType);
    Request post = new Builder().url(url).post(requestBody).build();
    try (Response response = client.newCall(post).execute()) {
      return Objects.requireNonNull(response.body()).string();
    } catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }

  /**
   * post发送流
   *
   * @return
   */
  public String postStream() {
    RequestBody requestBody = new RequestBody() {
      @Override
      public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
        bufferedSink.writeUtf8("#Demo\n")
            .writeUtf8("===================\n")
            .writeUtf8("hello world");
      }

      @Nullable
      @Override
      public MediaType contentType() {
        return MediaType.parse("text/x-markdown; charset=utf-8");
      }
    };
    Request post = new Builder().url(url).post(requestBody).build();
    try (Response response = client.newCall(post).execute()) {
      System.out.println(response);
      return Objects.requireNonNull(response.body()).string();
    } catch (IOException e) {
      e.printStackTrace();
      return "";
    }

  }


  /**
   * 上传文件
   *
   * @return
   * @throws IOException
   */
  public String postFile() throws IOException {
    String contentType = com.google.common.net.MediaType.MICROSOFT_EXCEL.toString();
    InputStream inputStream = this.getClass().getResourceAsStream("excel/demo.excel");

    Builder post = new Builder().url(url)
        .post(RequestBody.create(IoUtil.readBytes(inputStream), MediaType.parse(contentType)));
    Response response = client.newCall(post.build()).execute();
    response.close();
    return response.body().string();
  }

  /**
   * from表单提交
   *
   * @return
   */
  public String postFrom() {
    FormBody body = new FormBody.Builder().add("name", "张三").build();
    Request request = new Builder().url("https://example.com").post(body).build();
    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        // ... handle failed request
      }
      return response.body().string();
    } catch (IOException e) {
      // ... handle IO exception
    }
    return "";
  }

  /**
   * 多文件
   *
   * @return
   */
  public String postMultiPart() {
    InputStream inputStream = this.getClass().getResourceAsStream("excel/demo.excel");

    MultipartBody body = new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("images", "images")
        .addFormDataPart("excel", "demo.excel",
            RequestBody.create(new File("excel/demo.excel"), MediaType.parse(
                com.google.common.net.MediaType.MICROSOFT_EXCEL.toString()))).build();

    Request post = new Builder()
        .post(body).url(url)
        .build();

    try (Response response = client.newCall(post).execute()) {
      return response.body().toString();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }

  @Test
  public void cacheTest() {

    Request request = new Builder().url(url).build();

    try (Response response = client.newCall(request).execute()) {

      System.out.println("response:" + response);
      System.out.println("cache response:" + response.cacheResponse());
      System.out.println("networkCache response:" + response.networkResponse());

    } catch (IOException e) {
      e.printStackTrace();
    }

    try (Response response2 = client.newCall(request).execute()) {
      System.out.println("response2:" + response2);
      System.out.println("cache response2:" + response2.cacheResponse());
      System.out.println("networkCache response2:" + response2.networkResponse());
    } catch (IOException e) {
      e.printStackTrace();
    }

  }


}
