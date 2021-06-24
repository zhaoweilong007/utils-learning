package com.zwl.jsoup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

/**
 * 爬取
 *
 * @author zhao_wei_long
 * @since 2021/6/22
 **/
@Slf4j
public class JsoupTest {

  @Test
  public void crawling() throws IOException {

    String url = "https://www.zhihu.com";

    OkHttpClient client = new OkHttpClient().newBuilder().build();

    Map<String, String> hashMap = Maps.<String, String>newHashMap();
    hashMap.put("user-agent",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");
    String kv = "_zap=72b74b0b-f0b2-4952-95ff-6764c63b8b58; d_c0=\"APCcpYpN8hKPTlU5ChisJZ_ZqThdIzga32c=|1618278317\"; ISSW=1; __snaker__id=6BFIRdnhBJs1fa1e; _9755xjdesxxd_=32; YD00517437729195%3AWM_TID=UnX%2Fmr40r1dBERABEBJ6lbWIUrenrxqz; _xsrf=dpoDCYH6Y2gvCXxX3iuWDPduv9c8lXl0; tshl=; Hm_lvt_98beee57fd2ef70ccdd5ca52b9740c49=1622681978,1622694294,1623718869,1624323707; q_c1=9d36ba29791e4dbabcdbb0b2e039c935|1624355274000|1619164606000; tst=r; SESSIONID=jhtQpEM2c697kWjwj49nU5sKB3BjfI457BOPXyYzHk7; osd=UFoXBE3mBI62YD69EeMQUED0dksDjGz_5hR78CaBMreECm3XZnP7WNNkPrgVy4G8YmDDxM_8EbQ6KSETzpSrUTk=; JOID=VVEWAEPjD4-ybju2EOceVUv1ckUGh2376BFw8SKPN7yFDmPSbXL_VtZvP7wbzoq9Zm7Gz874H7ExKCUdy5-qVTc=; gdxidpyhxdE=EPxrT7%2BCvT%2Fd0GUa2UWfQvoqS8BDMegzydt%2FTPD5ahp6nPUeG0MEsr4x6PWtpneeuZP%2FDgzrrD35NDflz81%2BZGb4r1%5CgzhT%5CQEqn6yRbL1pNBsXM%2FbZSZRoDmkm7gSCjnaeNaXjvaWNM6mma78bkZ4VYW1oPSyM3yUUj07xcLYj27180%3A1624440353852; YD00517437729195%3AWM_NI=Z9%2BQGmf5B93JiuD8v%2BgwokgtxAtoBjn8u75sbsBC339gsdtZ02FXKZB25qmdadDjOkj1m2cRBjoSFx59%2BYNyUmCFSOgNxL1d346Iot2zTqqxOUBMR6cJyIMqMVdVazO%2FOFg%3D; YD00517437729195%3AWM_NIKE=9ca17ae2e6ffcda170e2e6ee99b45a9b9ff9b0fc48b6e78ea7c15a879b8e84aa7387ab9fa4c567f6f09f98d42af0fea7c3b92a9ae7e5b2c667a6eb8edab242abab8cd9f521a5a9a18ce680bc9fbc90c162aee8bbd8b879a2ad8ea4b239fbb9bd85f752f4ee8ab9ca5282eefb92f070a3bc8c8bce61aaefa3d3e25a82b6a1b4c574f7e7e5bacd7397b38da7d245a592b8adb470f1ecfaaaee80f2a69c92f4748d8b8eaed13ea196b7aad07b8f8aacb7e4728df181a7e637e2a3; captcha_session_v2=\"2|1:0|10:1624439457|18:captcha_session_v2|88:TDlFdHhXVE1HU0NSSlRkUUpmMHFHczBtd2IyR2cxQmdwOG4ray9wNS9PUmxKRlNnaWJsZmRlNXgxKy85QnJtVA==|fd8f2752362122b6aad8fdeec6744e89e3ed7c291599eab625fa961048ef6851\"; captcha_ticket_v2=\"2|1:0|10:1624439488|17:captcha_ticket_v2|704:eyJ2YWxpZGF0ZSI6IkNOMzFfOGMxbDJZdlB4Qko3VmhTWnh1Szk0OXlYanpJajF4WngyOE92Z3lPc0xBU3phOXcxMFdBQ1hZRF9mNXJIdTBjQkViYUZxWTlqdUc1Y050WmZKZjdMMXJNRHA2TnlGUkR1LUo0bVRjY0QyR0x4UC01S3c1OV9zMDhwQVhOcG5wTWF4ZHFtUmdpTldDRkRxeXdsbUpEUHBtTkFZaEFfWVB3MEotOWZhSWllZEhaNmlfcWlZc1p4N2dfbjQ5ME1oWVRhbDhQX0FXbXVjS0hiR2VIbEE2aFVVcGhGUVFmdWpDMkVaZXZTX0lXZzY3TmcxY3NIcFRseVV0bnN2ak0wWmdsRGYuS3pyWkZCX2twMlE1eDBJUWlMREFlUnNydVF6X2tIRkNsblh3aUZWRFZwNWxvUjBlTnNNTGpIQkpWNDFzLl9mLXcyaVVCV2dkemdBaVQ1WS50dGItV01UMUhxbjlSSUguWmNTcFlfN3IyVzE4ZEpOam5CVVJkODh6dC5RQzFxenE5SW5VWVNDY2s5Z190ZmJsT3dMWWVqRGJvdDFualpXZWNOLUNkMEJZNXZjSmpaWnVPNjdXQ0pVOVREREFud1ZscXE0UndqTWd0Y0NFRGNsT3MtWVlGUmhyb1JVRkdKRWV5WDJxb3Q2ZUdjYnVkUVN5aTRtalFqOXpYMyJ9|0ec8ef377ee01e06aad6600839b0ae834d3a6be51817ee7336f74b7983fc05b5\"; z_c0=\"2|1:0|10:1624439492|4:z_c0|92:Mi4xVG5jeEJRQUFBQUFBOEp5bGlrM3lFaVlBQUFCZ0FsVk54RWpBWVFBQ05Qbk5ZOGdpYld0Y2dkVTlLaENYZkQtVEtn|272351efe8ce3ce2b4c2470ca63d5193a2e06e3cf1bc506851b95ed96d101b45\"; KLBRSID=031b5396d5ab406499e2ac6fe1bb1a43|1624440938|1624439440";

    String cookie = "_zap=2ca50f0c-b83d-44ef-997d-8d2676fc2590; _xsrf=JY6DNS65LhUWFLk2i0C1RwPBp5FjJ3kR; ISSW=1; d_c0=\"AOCcZ9MnThOPTtspPSA9iCwSdvINf3DNPWg=|1624442445\"; Hm_lvt_98beee57fd2ef70ccdd5ca52b9740c49=1622694294,1623718869,1624323707,1624442447; captcha_session_v2=\"2|1:0|10:1624442446|18:captcha_session_v2|88:MEN5NElnSWtDQUc2SFlxaTFkMjd3ZzhHcXJ5aHM4NFZoR09tbEdXcXdsNWJMeGVqdXlBVlQ1clBWc2NyQXlrcA==|485e8281e3697eaffa46f46edcd9c44f1f6a07daddbd02cdaf2f71252afe53d4\"; __snaker__id=rEfQskRlNA2LnQhd; _9755xjdesxxd_=32; gdxidpyhxdE=Bwr9nH0OBpgLTeBwrkOGYrcBfwYpgjlQQuC2OYYKaP0NV1Eh9R0BEsCZiV%2FtN%2FiqzMBVUD98Bb%2BXC90tAWlZXomKfQOybSZqGScSBXTYuioMyuL38mYKL3rfyRvSA%5CiIa%2BuAYu5JK7mokhLYl0ZcJzg2NBZ%5CHWyOgiLseDbODuQohgIk%3A1624443349222; YD00517437729195%3AWM_TID=UnX%2Fmr40r1dBERABEBJ6lbWIUrenrxqz; captcha_ticket_v2=\"2|1:0|10:1624442456|17:captcha_ticket_v2|704:eyJ2YWxpZGF0ZSI6IkNOMzFfdVVEVVQ1Ll9LX0J1OGo0dWxhYkliSTRUQU54aFBpRUV0UldfQUJVQ0QxMUdxaWJMNEtZSnE4MU44ekl0YW9NLkRtLnQxS3dXSmVPbE0uUlNmY1NWWnVOYU1IVDB3UGZ3Q2hJLW5OR0N4ZFRRTWhxbW0wcXAuMkEucFhiaXRlNF9NU0d1UW5FTmYyS0JlMDdjVkt3S3dsemlrNUROT0lCYVVnQ201amUxVktVbURNZ0M1cWFLLW50MFNXam43MFdYaTJNNzZoRVBTQjcxVnRSeWhacWJkVkdrWmNnMlhzOUQ3emNHZUc5bWJhTm9TbnhyRGFLcnU5Q05oblRhMWdkVm1fa1lhSjZ4ekZyZDdJa3JlUUtLZUdpNE4tY3Z0Snhtd04yd18xNGRvSWEwdGs5Lmx5cEhpQ3ZXb2lIdzVoSl9lank1eU15WDR1aGZrMjVnU09ubFZQNGNDbHN4ZzBXX1EuQXk1ZzhaSzlSd0F1TS1sZk01emF1YnhHYS5valZXcm03SGtRRG0uZDVTNm8ucmFKVnVHRmp4YTJDa0NCalBLQUQubnFzMGxra0gwLTV6SjhHLThiVnFhNTZMa19vckZmSEs3N2RSWWNkc00uLlpVejFZTmNEVWg2d0RHb3NTV2h4YS42el8wckthdnoxa1phOHFlVndKZy5JMyJ9|048b80b7d451a90ee60cf0eee21988d0989695b15c80138c768d417ff4dba4e2\"; z_c0=\"2|1:0|10:1624442456|4:z_c0|92:Mi4xVG5jeEJRQUFBQUFBNEp4bjB5ZE9FeVlBQUFCZ0FsVk5XRlRBWVFDdFdRdVdFQ3RDX2E1bGx4a2lJdDgzREgxajVn|7d174d01486ed018ce953f92cf39d1b4078c20e08f34bc7389ae6c27ed16e6d9\"; tst=r; q_c1=0517f81838874dc7ae5ca1d825ddfa26|1624498155000|1624498155000; SESSIONID=tKB4lWFYewyIfJNyqtmORO5o7hQJNiVTdUkJba6aG9p; JOID=UloXAE0_vltxI401bzyqh4O3w8N_SfUYQEnYczdcyyI5QMF5FuZdjBInjzRrL_I6fMUol5DLFkA8203cNKcSjrk=; osd=U1oVB0g-vll2Jow1bTuvhoO1xMZ-SfcfRUjYcTBZyiI7R8R4FuRaiRMnjTNuLvI4e8Apl5LME0E82UrZNacQibw=; KLBRSID=e42bab774ac0012482937540873c03cf|1624504408|1624498154";

    String cookie2 = "KLBRSID=fe78dd346df712f9c4f126150949b853|1624442461|1624442444";
    hashMap.put("cookie", cookie);
    hashMap.put("accept",
        "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
//    hashMap.put("accept-encoding", "gzip, deflate, br");
//    hashMap.put("accept-language", "zh-CN,zh;q=0.9");

    Headers headers = Headers
        .of(hashMap);

    Request request = new Builder().url(url).get().
        headers(headers).build();
    String string = client.newCall(request).execute().body().string();

    System.out.println(string);


  }


  @Test
  public void webCrawling() throws IOException {
    WebCrawling webCrawling = new WebCrawling(new LinkedBlockingQueue<>(100));
    webCrawling.start("");
  }

  private static final String topicUrl = "https://www.zhihu.com/topics";
  public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36";

  private static final String topicList = "https://www.zhihu.com/node/TopicsPlazzaListV2";

  @Test
  public void parseTopic() throws IOException {
    //解析话题
    Document document = Jsoup.connect(topicUrl)
        .userAgent(USER_AGENT)
//          .cookie("cookie", cookie)
        .get();

    Elements elements = document.select(".zm-topic-cat-item");
    List<Topic> topics = elements.stream()
        .map(element -> new Topic(element.attr("data-id"), element.child(0).text()))
        .collect(Collectors.toList());
    iterTopic(topics);

  }


  @AllArgsConstructor
  @NoArgsConstructor
  static class Topic {

    String id;
    String desc;
  }


  private void iterTopic(List<Topic> list) throws IOException {
    CopyOnWriteArrayList<Topic> arrayList = Lists.<Topic>newCopyOnWriteArrayList(list);

    parseTopic(list, arrayList);

    arrayList.forEach(System.out::println);
  }

  public List<Topic> parse(String id) throws IOException {
    List<Topic> topics = new ArrayList<>();
    FormBody body = new FormBody.Builder()
        .add("method", "next")
        .add("params",
            "{\"topic_id\":" + id + ",\"offset\":100,\"hash_id\":\"\"}")
        .build();
    Request request = new Builder().url(topicList).post(body).build();
    String json = new OkHttpClient().newCall(request).execute().body().string();
    JSONObject jsonObject = JSON.parseObject(json);
    JSONArray msg = jsonObject.getJSONArray("msg");

    return msg.stream().map(o -> {
      String obj = (String) o;
      Topic topic = new Topic();
      int index = obj.indexOf("href=\"/topic/");
      int start = obj.indexOf("<strong>");
      int end = obj.indexOf("</strong>");
      topic.id = obj.substring(index,index+8);
      topic.desc = obj.substring(start,end);
      return topic;
    }).collect(Collectors.toList());
  }


  public void parseTopic(List<Topic> topics, List<Topic> result) throws IOException {
    for (Topic topic : topics) {
      List<Topic> list = parse(topic.id);
      if (!list.isEmpty()) {
        parseTopic(list, result);
      } else {
        result.add(topic);
      }
    }

  }

  @Test
  public void json() {
    String json = "{\"r\":0,\n"
        + " \"msg\": [\"<div class=\\\"item\\\"><div class=\\\"blk\\\">\\n<a target=\\\"_blank\\\" href=\\\"\\/topic\\/19618685\\\">\\n<img src=\\\"https:\\/\\/pic1.zhimg.com\\/0f4a70d981395f5cb69a56eac8059df7_l.png\\\" alt=\\\"\\u5ba4\\u5185\\u88c5\\u4fee\\\">\\n<strong>\\u5ba4\\u5185\\u88c5\\u4fee<\\/strong>\\n<\\/a>\\n<p>\\u5ba4\\u5185\\u88c5\\u4fee\\u5305\\u62ec\\u623f\\u95f4\\u8bbe\\u8ba1\\u3001\\u88c5\\u4fee\\u3001\\u5bb6\\u5177\\u5e03\\u7f6e\\u53ca\\u5404\\u79cd\\u5c0f\\u88c5\\u70b9\\u3002\\u504f\\u91cd\\u4e8e\\u5efa\\u7b51\\u7269\\u2026<\\/p>\\n\\n<a id=\\\"t::-22882\\\" href=\\\"javascript:;\\\" class=\\\"follow meta-item zg-follow\\\"><i class=\\\"z-icon-follow\\\"><\\/i>\\u5173\\u6ce8<\\/a>\\n\\n<\\/div><\\/div>\",\"<div class=\\\"item\\\"><div class=\\\"blk\\\">\\n<a target=\\\"_blank\\\" href=\\\"\\/topic\\/19769983\\\">\\n<img src=\\\"https:\\/\\/pic3.zhimg.com\\/6db301332_l.jpg\\\" alt=\\\"\\u8c22\\u718a\\u732b\\u541b\\u98df\\u8c31\\\">\\n<strong>\\u8c22\\u718a\\u732b\\u541b\\u98df\\u8c31<\\/strong>\\n<\\/a>\\n<p><\\/p>\\n\\n<a id=\\\"t::-73446\\\" href=\\\"javascript:;\\\" class=\\\"follow meta-item zg-follow\\\"><i class=\\\"z-icon-follow\\\"><\\/i>\\u5173\\u6ce8<\\/a>\\n\\n<\\/div><\\/div>\",\"<div class=\\\"item\\\"><div class=\\\"blk\\\">\\n<a target=\\\"_blank\\\" href=\\\"\\/topic\\/19557644\\\">\\n<img src=\\\"https:\\/\\/pic1.zhimg.com\\/v2-778a406ffe285f5922d48bc74e8ac003_l.jpg\\\" alt=\\\"\\u95f4\\u9694\\u5e74\\uff08Gap Year\\uff09\\\">\\n<strong>\\u95f4\\u9694\\u5e74\\uff08Gap Year\\uff09<\\/strong>\\n<\\/a>\\n<p>Gap Year\\u610f\\u4e3a\\u95f4\\u9694\\u5e74\\u3001\\u7a7a\\u6863\\u5e74\\uff0c\\u662f\\u897f\\u65b9\\u793e\\u4f1a\\u901a\\u8fc7\\u8fd1\\u4ee3\\u4e16\\u754c\\u9752\\u5e74\\u2026<\\/p>\\n\\n<a id=\\\"t::-2496\\\" href=\\\"javascript:;\\\" class=\\\"follow meta-item zg-follow\\\"><i class=\\\"z-icon-follow\\\"><\\/i>\\u5173\\u6ce8<\\/a>\\n\\n<\\/div><\\/div>\",\"<div class=\\\"item\\\"><div class=\\\"blk\\\">\\n<a target=\\\"_blank\\\" href=\\\"\\/topic\\/19585358\\\">\\n<img src=\\\"https:\\/\\/pic2.zhimg.com\\/v2-77bebabb7d2330b6797bf56f21cdfd8f_l.jpg\\\" alt=\\\"\\u6781\\u7b80\\u4e3b\\u4e49\\uff08Minimalism\\uff09\\\">\\n<strong>\\u6781\\u7b80\\u4e3b\\u4e49\\uff08Minimalism\\uff09<\\/strong>\\n<\\/a>\\n<p>\\u6781\\u7b80\\u4e3b\\u4e49\\u662f\\u4e00\\u79cd\\u751f\\u6d3b\\u4ee5\\u53ca\\u827a\\u672f\\u4e0a\\u7684\\u98ce\\u683c\\uff0c\\u672c\\u4e49\\u4e3a\\u8ffd\\u6c42\\u6781\\u81f4\\u7b80\\u7ea6\\u7684\\u5448\\u73b0\\u6548\\u2026<\\/p>\\n\\n<a id=\\\"t::-11707\\\" href=\\\"javascript:;\\\" class=\\\"follow meta-item zg-follow\\\"><i class=\\\"z-icon-follow\\\"><\\/i>\\u5173\\u6ce8<\\/a>\\n\\n<\\/div><\\/div>\",\"<div class=\\\"item\\\"><div class=\\\"blk\\\">\\n<a target=\\\"_blank\\\" href=\\\"\\/topic\\/19552189\\\">\\n<img src=\\\"https:\\/\\/pic3.zhimg.com\\/02b2e03bd_l.jpg\\\" alt=\\\"\\u5496\\u5561\\u9986\\\">\\n<strong>\\u5496\\u5561\\u9986<\\/strong>\\n<\\/a>\\n<p>\\u5496\\u5561\\u5e97\\uff08\\u6cd5\\u8bed\\uff1acaf\\u00e9\\uff0c\\u82f1\\u8bed\\uff1acoffee shop \\u6216 co\\u2026<\\/p>\\n\\n<a id=\\\"t::-657\\\" href=\\\"javascript:;\\\" class=\\\"follow meta-item zg-follow\\\"><i class=\\\"z-icon-follow\\\"><\\/i>\\u5173\\u6ce8<\\/a>\\n\\n<\\/div><\\/div>\",\"<div class=\\\"item\\\"><div class=\\\"blk\\\">\\n<a target=\\\"_blank\\\" href=\\\"\\/topic\\/19610023\\\">\\n<img src=\\\"https:\\/\\/pic2.zhimg.com\\/1bcf2223d_l.jpg\\\" alt=\\\"\\u9910\\u996e\\u4e1a\\\">\\n<strong>\\u9910\\u996e\\u4e1a<\\/strong>\\n<\\/a>\\n<p>\\u9910\\u996e\\u4e1a\\uff08catering\\uff09\\u662f\\u901a\\u8fc7\\u5373\\u65f6\\u52a0\\u5de5\\u5236\\u4f5c\\u3001\\u5546\\u4e1a\\u9500\\u552e\\u548c\\u670d\\u52a1\\u6027\\u2026<\\/p>\\n\\n<a id=\\\"t::-19999\\\" href=\\\"javascript:;\\\" class=\\\"follow meta-item zg-follow\\\"><i class=\\\"z-icon-follow\\\"><\\/i>\\u5173\\u6ce8<\\/a>\\n\\n<\\/div><\\/div>\",\"<div class=\\\"item\\\"><div class=\\\"blk\\\">\\n<a target=\\\"_blank\\\" href=\\\"\\/topic\\/19573372\\\">\\n<img src=\\\"https:\\/\\/pic4.zhimg.com\\/68752aab8_l.jpg\\\" alt=\\\"\\u83dc\\u8c31\\\">\\n<strong>\\u83dc\\u8c31<\\/strong>\\n<\\/a>\\n<p>\\u83dc\\u8c31\\u662f\\u70f9\\u8c03\\u53a8\\u5e08\\u5229\\u7528\\u5404\\u79cd\\u70f9\\u996a\\u539f\\u6599\\u3001\\u901a\\u8fc7\\u5404\\u79cd\\u70f9\\u8c03\\u6280\\u6cd5\\u521b\\u4f5c\\u51fa\\u7684\\u67d0\\u4e00\\u83dc\\u2026<\\/p>\\n\\n<a id=\\\"t::-7720\\\" href=\\\"javascript:;\\\" class=\\\"follow meta-item zg-follow\\\"><i class=\\\"z-icon-follow\\\"><\\/i>\\u5173\\u6ce8<\\/a>\\n\\n<\\/div><\\/div>\",\"<div class=\\\"item\\\"><div class=\\\"blk\\\">\\n<a target=\\\"_blank\\\" href=\\\"\\/topic\\/19555489\\\">\\n<img src=\\\"https:\\/\\/pic4.zhimg.com\\/cb9aabba8_l.jpg\\\" alt=\\\"\\u8461\\u8404\\u9152\\\">\\n<strong>\\u8461\\u8404\\u9152<\\/strong>\\n<\\/a>\\n<p>\\u8461\\u8404\\u9152\\u662f\\u4ee5\\u8461\\u8404\\u4e3a\\u539f\\u6599\\u917f\\u9020\\u7684\\u4e00\\u79cd\\u679c\\u9152\\u3002\\u5176\\u9152\\u7cbe\\u5ea6\\u9ad8\\u4e8e\\u5564\\u9152\\u800c\\u4f4e\\u4e8e\\u767d\\u9152\\u2026<\\/p>\\n\\n<a id=\\\"t::-1749\\\" href=\\\"javascript:;\\\" class=\\\"follow meta-item zg-follow\\\"><i class=\\\"z-icon-follow\\\"><\\/i>\\u5173\\u6ce8<\\/a>\\n\\n<\\/div><\\/div>\",\"<div class=\\\"item\\\"><div class=\\\"blk\\\">\\n<a target=\\\"_blank\\\" href=\\\"\\/topic\\/19650466\\\">\\n<img src=\\\"https:\\/\\/pic3.zhimg.com\\/v2-ab5da638c250186af95ed4258686b041_l.jpg\\\" alt=\\\"\\u70f9\\u996a\\u5e38\\u8bc6\\\">\\n<strong>\\u70f9\\u996a\\u5e38\\u8bc6<\\/strong>\\n<\\/a>\\n<p>\\u90a3\\u4e9b\\u8033\\u719f\\u80fd\\u8be6\\u7684\\u7f8e\\u98df\\u7a8d\\u95e8\\u3002<\\/p>\\n\\n<a id=\\\"t::-33496\\\" href=\\\"javascript:;\\\" class=\\\"follow meta-item zg-follow\\\"><i class=\\\"z-icon-follow\\\"><\\/i>\\u5173\\u6ce8<\\/a>\\n\\n<\\/div><\\/div>\",\"<div class=\\\"item\\\"><div class=\\\"blk\\\">\\n<a target=\\\"_blank\\\" href=\\\"\\/topic\\/19575374\\\">\\n<img src=\\\"https:\\/\\/pic3.zhimg.com\\/80d4329c6_l.jpg\\\" alt=\\\"\\u53a8\\u623f\\\">\\n<strong>\\u53a8\\u623f<\\/strong>\\n<\\/a>\\n<p>\\u53a8\\u623f\\uff0c\\u662f\\u6307\\u53ef\\u5728\\u5185\\u51c6\\u5907\\u98df\\u7269\\u5e76\\u8fdb\\u884c\\u70f9\\u996a\\u7684\\u623f\\u95f4\\uff0c\\u4e00\\u4e2a\\u73b0\\u4ee3\\u5316\\u7684\\u53a8\\u623f\\u901a\\u5e38\\u2026<\\/p>\\n\\n<a id=\\\"t::-8393\\\" href=\\\"javascript:;\\\" class=\\\"follow meta-item zg-follow\\\"><i class=\\\"z-icon-follow\\\"><\\/i>\\u5173\\u6ce8<\\/a>\\n\\n<\\/div><\\/div>\",\"<div class=\\\"item\\\"><div class=\\\"blk\\\">\\n<a target=\\\"_blank\\\" href=\\\"\\/topic\\/19666493\\\">\\n<img src=\\\"https:\\/\\/pic2.zhimg.com\\/f15a16b7d_l.jpg\\\" alt=\\\"\\u7537\\u6027\\u7740\\u88c5\\\">\\n<strong>\\u7537\\u6027\\u7740\\u88c5<\\/strong>\\n<\\/a>\\n<p><\\/p>\\n\\n<a id=\\\"t::-38846\\\" href=\\\"javascript:;\\\" class=\\\"follow meta-item zg-follow\\\"><i class=\\\"z-icon-follow\\\"><\\/i>\\u5173\\u6ce8<\\/a>\\n\\n<\\/div><\\/div>\",\"<div class=\\\"item\\\"><div class=\\\"blk\\\">\\n<a target=\\\"_blank\\\" href=\\\"\\/topic\\/19582090\\\">\\n<img src=\\\"https:\\/\\/pic2.zhimg.com\\/48869342794672518f1c56225641b124_l.jpg\\\" alt=\\\"\\u7537\\u58eb\\\">\\n<strong>\\u7537\\u58eb<\\/strong>\\n<\\/a>\\n<p><\\/p>\\n\\n<a id=\\\"t::-10600\\\" href=\\\"javascript:;\\\" class=\\\"follow meta-item zg-follow\\\"><i class=\\\"z-icon-follow\\\"><\\/i>\\u5173\\u6ce8<\\/a>\\n\\n<\\/div><\\/div>\",\"<div class=\\\"item\\\"><div class=\\\"blk\\\">\\n<a target=\\\"_blank\\\" href=\\\"\\/topic\\/19766512\\\">\\n<img src=\\\"https:\\/\\/pic4.zhimg.com\\/v2-c862d0fb124f78bbd5224e62a6e64ce8_l.jpg\\\" alt=\\\"\\u5468\\u672b\\u5ea6\\u5047\\\">\\n<strong>\\u5468\\u672b\\u5ea6\\u5047<\\/strong>\\n<\\/a>\\n<p><\\/p>\\n\\n<a id=\\\"t::-72273\\\" href=\\\"javascript:;\\\" class=\\\"follow meta-item zg-follow\\\"><i class=\\\"z-icon-follow\\\"><\\/i>\\u5173\\u6ce8<\\/a>\\n\\n<\\/div><\\/div>\",\"<div class=\\\"item\\\"><div class=\\\"blk\\\">\\n<a target=\\\"_blank\\\" href=\\\"\\/topic\\/19557333\\\">\\n<img src=\\\"https:\\/\\/pic1.zhimg.com\\/94ddbb433_l.jpg\\\" alt=\\\"\\u5bb6\\u5177\\\">\\n<strong>\\u5bb6\\u5177<\\/strong>\\n<\\/a>\\n<p>\\u5bb6\\u5177\\u662f\\u6307\\u4eba\\u7c7b\\u7ef4\\u6301\\u6b63\\u5e38\\u751f\\u6d3b\\u3001\\u4ece\\u4e8b\\u751f\\u4ea7\\u5b9e\\u8df5\\u548c\\u5f00\\u5c55\\u793e\\u4f1a\\u6d3b\\u52a8\\u5fc5\\u4e0d\\u53ef\\u5c11\\u7684\\u2026<\\/p>\\n\\n<a id=\\\"t::-2382\\\" href=\\\"javascript:;\\\" class=\\\"follow meta-item zg-follow\\\"><i class=\\\"z-icon-follow\\\"><\\/i>\\u5173\\u6ce8<\\/a>\\n\\n<\\/div><\\/div>\",\"<div class=\\\"item\\\"><div class=\\\"blk\\\">\\n<a target=\\\"_blank\\\" href=\\\"\\/topic\\/19595031\\\">\\n<img src=\\\"https:\\/\\/pic2.zhimg.com\\/4fcb311e0_l.jpg\\\" alt=\\\"\\u751c\\u54c1\\\">\\n<strong>\\u751c\\u54c1<\\/strong>\\n<\\/a>\\n<p>\\u751c\\u54c1\\uff0c\\u4e5f\\u53eb\\u751c\\u70b9\\uff0c\\u662f\\u4e00\\u4e2a\\u5f88\\u5e7f\\u7684\\u6982\\u5ff5\\uff0c\\u5927\\u81f4\\u5206\\u4e3a\\u751c\\u5473\\u70b9\\u5fc3\\u548c\\u5e7f\\u5f0f\\u7684\\u7cd6\\u6c34\\u2026<\\/p>\\n\\n<a id=\\\"t::-14928\\\" href=\\\"javascript:;\\\" class=\\\"follow meta-item zg-follow\\\"><i class=\\\"z-icon-follow\\\"><\\/i>\\u5173\\u6ce8<\\/a>\\n\\n<\\/div><\\/div>\",\"<div class=\\\"item\\\"><div class=\\\"blk\\\">\\n<a target=\\\"_blank\\\" href=\\\"\\/topic\\/19581985\\\">\\n<img src=\\\"https:\\/\\/pic1.zhimg.com\\/v2-635f0017f024042ac2ecef09056ddecd_l.jpg\\\" alt=\\\"\\u65c5\\u6e38\\u63a8\\u8350\\\">\\n<strong>\\u65c5\\u6e38\\u63a8\\u8350<\\/strong>\\n<\\/a>\\n<p><\\/p>\\n\\n<a id=\\\"t::-10569\\\" href=\\\"javascript:;\\\" class=\\\"follow meta-item zg-follow\\\"><i class=\\\"z-icon-follow\\\"><\\/i>\\u5173\\u6ce8<\\/a>\\n\\n<\\/div><\\/div>\",\"<div class=\\\"item\\\"><div class=\\\"blk\\\">\\n<a target=\\\"_blank\\\" href=\\\"\\/topic\\/19752767\\\">\\n<img src=\\\"https:\\/\\/pic1.zhimg.com\\/v2-328221e362c00211d1e5db5c62854962_l.jpg\\\" alt=\\\"\\u65c5\\u6e38\\u4e66\\u7c4d\\\">\\n<strong>\\u65c5\\u6e38\\u4e66\\u7c4d<\\/strong>\\n<\\/a>\\n<p><\\/p>\\n\\n<a id=\\\"t::-67661\\\" href=\\\"javascript:;\\\" class=\\\"follow meta-item zg-follow\\\"><i class=\\\"z-icon-follow\\\"><\\/i>\\u5173\\u6ce8<\\/a>\\n\\n<\\/div><\\/div>\",\"<div class=\\\"item\\\"><div class=\\\"blk\\\">\\n<a target=\\\"_blank\\\" href=\\\"\\/topic\\/19868718\\\">\\n<img src=\\\"https:\\/\\/pic2.zhimg.com\\/v2-3db532ebcbbfbb8784ff1cd17aaf559b_l.jpg\\\" alt=\\\"\\u5973\\u88c5\\u642d\\u914d\\\">\\n<strong>\\u5973\\u88c5\\u642d\\u914d<\\/strong>\\n<\\/a>\\n<p><\\/p>\\n\\n<a id=\\\"t::-108264\\\" href=\\\"javascript:;\\\" class=\\\"follow meta-item zg-follow\\\"><i class=\\\"z-icon-follow\\\"><\\/i>\\u5173\\u6ce8<\\/a>\\n\\n<\\/div><\\/div>\",\"<div class=\\\"item\\\"><div class=\\\"blk\\\">\\n<a target=\\\"_blank\\\" href=\\\"\\/topic\\/19644670\\\">\\n<img src=\\\"https:\\/\\/pic3.zhimg.com\\/v2-f2dea7d59358ad1970955c40c4c79752_l.jpg\\\" alt=\\\"\\u65f6\\u5c1a\\u7a7f\\u8863\\\">\\n<strong>\\u65f6\\u5c1a\\u7a7f\\u8863<\\/strong>\\n<\\/a>\\n<p><\\/p>\\n\\n<a id=\\\"t::-31552\\\" href=\\\"javascript:;\\\" class=\\\"follow meta-item zg-follow\\\"><i class=\\\"z-icon-follow\\\"><\\/i>\\u5173\\u6ce8<\\/a>\\n\\n<\\/div><\\/div>\",\"<div class=\\\"item\\\"><div class=\\\"blk\\\">\\n<a target=\\\"_blank\\\" href=\\\"\\/topic\\/19569509\\\">\\n<img src=\\\"https:\\/\\/pic3.zhimg.com\\/v2-c667681e61d123c49477425cdba91787_l.jpg\\\" alt=\\\"\\u8336\\u53f6\\\">\\n<strong>\\u8336\\u53f6<\\/strong>\\n<\\/a>\\n<p>\\u8336\\u53f6\\uff0c\\u6307\\u8336\\u6811\\u7684\\u53f6\\u5b50\\u548c\\u82bd\\u3002\\u522b\\u540d\\u8336\\u3001\\u69da\\uff08ji\\u01ce\\uff09\\uff0c\\u8317\\uff0c\\u8348\\uff08chu\\u01ce\\u2026<\\/p>\\n\\n<a id=\\\"t::-6439\\\" href=\\\"javascript:;\\\" class=\\\"follow meta-item zg-follow\\\"><i class=\\\"z-icon-follow\\\"><\\/i>\\u5173\\u6ce8<\\/a>\\n\\n<\\/div><\\/div>\"]\n"
        + "}";

    JSONObject jsonObject = JSON.parseObject(json);

    System.out.println(jsonObject);
  }

}
