package com.zwl.http.httpclient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

/**
 * @author ZhaoWeiLong
 * @date 2021/6/1
 **/
@Slf4j
public class HttpClientTest {

  private final JSONResponseHandler jsonResponseHandler = new JSONResponseHandler();

  private final String url = "";

  private CloseableHttpClient client = HttpClients.createDefault();


  private final RequestConfig requestConfig = RequestConfig.custom()
      //连接超时
      .setConnectTimeout(5000)
      //请求超时
      .setConnectionRequestTimeout(5000)
      //读取超时
      .setSocketTimeout(6000)
      .build();

  /**
   * 统一响应处理器
   */
  static class JSONResponseHandler implements ResponseHandler<JSONObject> {

    @SneakyThrows
    @Override
    public JSONObject handleResponse(HttpResponse httpResponse)
        throws ClientProtocolException, IOException {
      int statusCode = httpResponse.getStatusLine().getStatusCode();
      if (statusCode == HttpStatus.SC_OK) {
        return JSON
            .parseObject(EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8));
      } else {
        throw new HttpException("");
      }
    }
  }

  @Test
  public void sendGet() throws URISyntaxException {

    URI uri = new URIBuilder()
        .setScheme("http")
        .setHost("www.google.com")
//    uriBuilder.setPort(80);
        .setPath("/search")
        .setParameter("q", "httpclient").build();

    RequestConfig config = RequestConfig.copy(requestConfig)
        .setProxy(new HttpHost("http://127.0.0.1:7890")).build();
    HttpClientContext context = HttpClientContext.create();
    HttpGet httpGet = new HttpGet(uri);
    httpGet.setConfig(config);
    httpGet.setHeader("Accept", "*/*");
    try {
      JSONObject object = client.execute(httpGet, jsonResponseHandler, context);
      System.out.println(object.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void sendPost() {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpPost httpPost = new HttpPost("https://example.com");
      httpPost.setEntity(new StringEntity("{\"name\": \"张三\"}", ContentType.APPLICATION_JSON));
      String responseBody = httpClient.execute(httpPost, httpResponse -> {
        int status = httpResponse.getStatusLine().getStatusCode();
        if (status < 200 || status >= 300) {
          // ... handle unsuccessful request
        }
        HttpEntity entity = httpResponse.getEntity();
        return entity != null ? EntityUtils.toString(entity) : null;
      });
      // ... do something with response

    } catch (IOException e) {
      // ... handle IO exception
    }

  }

  @Test
  public void sendFormPost() throws UnsupportedEncodingException {
    ArrayList<NameValuePair> formparams = new ArrayList<>();
    formparams.add(new BasicNameValuePair("userName", "admin"));
    formparams.add(new BasicNameValuePair("password", "admin123"));

    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(formparams);

    HttpPost post = new HttpPost(url);

    post.setEntity(urlEncodedFormEntity);
    post.setConfig(requestConfig);

    try {
      CloseableHttpResponse response = client.execute(post);
      Header[] headers = response.getAllHeaders();
      System.out.println(Arrays.toString(headers));
      System.out.println(response.getEntity().getContentType());
      System.out.println(response.getEntity().toString());
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Test
  public void manager() {
    //多线程连接管理器
    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    //最大连接数
    connectionManager.setMaxTotal(100);
    //默认最大连接数
    connectionManager.setDefaultMaxPerRoute(50);
    connectionManager.setDefaultSocketConfig(SocketConfig.DEFAULT);
    connectionManager.setDefaultConnectionConfig(ConnectionConfig.DEFAULT);

//    connectionManager.setValidateAfterInactivity(0);

    //client是线程安全的 只要一个实例即可，连接连接池管理连接
    CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager)
        .setDefaultRequestConfig(requestConfig)
        .addInterceptorFirst(new HttpRequestInterceptor() {
          @Override
          public void process(HttpRequest httpRequest, HttpContext httpContext)
              throws HttpException, IOException {
            log.info("执行请求前拦截器");
            httpRequest.setHeader("token", "adadadad");
          }
        })
        .addInterceptorFirst(new HttpResponseInterceptor() {
          @Override
          public void process(HttpResponse httpResponse, HttpContext httpContext)
              throws HttpException, IOException {
            log.info("执行请求后拦截器");
            String toString = EntityUtils.toString(httpResponse.getEntity());
          }
        })
        .build();

//    client.execute()
//    client.close();
  }

  @Test
  public void basicManager() {
    //简单连接管理器，一次只能处理一个连接
    BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager();
    HttpRoute localhost = new HttpRoute(new HttpHost("localhost", 80));
    ConnectionRequest connectionRequest = connectionManager.requestConnection(localhost, null);
    HttpClientContext context = HttpClientContext.create();
    try {
      HttpClientConnection connection = connectionRequest.get(10, TimeUnit.SECONDS);
      if (!connection.isOpen()) {
        //没有连接 建立连接
        connectionManager.connect(connection,
            localhost,
            1000,
            context);
        connectionManager.routeComplete(connection, localhost, context);
      }
      //发起请求

      HttpGet httpGet = new HttpGet();
      connection.sendRequestHeader(httpGet);
//      connection.receiveResponseEntity();

    } catch (InterruptedException | ExecutionException | IOException | HttpException e) {
      e.printStackTrace();
    } finally {

    }
  }


  /**
   * 流API 灵活简单，不需要处理连接管理和资源释放
   */
  @Test
  public void fluentAPITest() throws IOException {

    String res = Request.Get("http://somehost/")
        .connectTimeout(1000)
        .socketTimeout(1000)
        .execute().returnContent().asString();

    String response = Request.Post("http://localhost")
        .useExpectContinue()
        .version(HttpVersion.HTTP_1_1)
        .bodyString("test demo", ContentType.DEFAULT_TEXT)
        .execute().handleResponse(new ResponseHandler<String>() {
          @Override
          public String handleResponse(HttpResponse httpResponse)
              throws ClientProtocolException, IOException {
            return EntityUtils.toString(httpResponse.getEntity());
          }
        });

    Request.Post("http://localhost")
        .addHeader("Accept", "*/*")
        .bodyForm(Form.form().add("a", "asdad").add("b", "dadadada").build())
        .execute().saveContent(new File("resp.txt"));


  }

}
