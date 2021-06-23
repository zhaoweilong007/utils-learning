package com.zwl.jsoup;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
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

    String url = "https://www.zhihu.com/";

    OkHttpClient client = new OkHttpClient().newBuilder().build();

    Map<String, String> hashMap = Maps.<String, String>newHashMap();
    hashMap.put("user-agent",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");
    String kv = "_zap=72b74b0b-f0b2-4952-95ff-6764c63b8b58; d_c0=\"APCcpYpN8hKPTlU5ChisJZ_ZqThdIzga32c=|1618278317\"; ISSW=1; __snaker__id=6BFIRdnhBJs1fa1e; _9755xjdesxxd_=32; YD00517437729195%3AWM_TID=UnX%2Fmr40r1dBERABEBJ6lbWIUrenrxqz; _xsrf=dpoDCYH6Y2gvCXxX3iuWDPduv9c8lXl0; tshl=; Hm_lvt_98beee57fd2ef70ccdd5ca52b9740c49=1622681978,1622694294,1623718869,1624323707; q_c1=9d36ba29791e4dbabcdbb0b2e039c935|1624355274000|1619164606000; tst=r; SESSIONID=jhtQpEM2c697kWjwj49nU5sKB3BjfI457BOPXyYzHk7; osd=UFoXBE3mBI62YD69EeMQUED0dksDjGz_5hR78CaBMreECm3XZnP7WNNkPrgVy4G8YmDDxM_8EbQ6KSETzpSrUTk=; JOID=VVEWAEPjD4-ybju2EOceVUv1ckUGh2376BFw8SKPN7yFDmPSbXL_VtZvP7wbzoq9Zm7Gz874H7ExKCUdy5-qVTc=; gdxidpyhxdE=EPxrT7%2BCvT%2Fd0GUa2UWfQvoqS8BDMegzydt%2FTPD5ahp6nPUeG0MEsr4x6PWtpneeuZP%2FDgzrrD35NDflz81%2BZGb4r1%5CgzhT%5CQEqn6yRbL1pNBsXM%2FbZSZRoDmkm7gSCjnaeNaXjvaWNM6mma78bkZ4VYW1oPSyM3yUUj07xcLYj27180%3A1624440353852; YD00517437729195%3AWM_NI=Z9%2BQGmf5B93JiuD8v%2BgwokgtxAtoBjn8u75sbsBC339gsdtZ02FXKZB25qmdadDjOkj1m2cRBjoSFx59%2BYNyUmCFSOgNxL1d346Iot2zTqqxOUBMR6cJyIMqMVdVazO%2FOFg%3D; YD00517437729195%3AWM_NIKE=9ca17ae2e6ffcda170e2e6ee99b45a9b9ff9b0fc48b6e78ea7c15a879b8e84aa7387ab9fa4c567f6f09f98d42af0fea7c3b92a9ae7e5b2c667a6eb8edab242abab8cd9f521a5a9a18ce680bc9fbc90c162aee8bbd8b879a2ad8ea4b239fbb9bd85f752f4ee8ab9ca5282eefb92f070a3bc8c8bce61aaefa3d3e25a82b6a1b4c574f7e7e5bacd7397b38da7d245a592b8adb470f1ecfaaaee80f2a69c92f4748d8b8eaed13ea196b7aad07b8f8aacb7e4728df181a7e637e2a3; captcha_session_v2=\"2|1:0|10:1624439457|18:captcha_session_v2|88:TDlFdHhXVE1HU0NSSlRkUUpmMHFHczBtd2IyR2cxQmdwOG4ray9wNS9PUmxKRlNnaWJsZmRlNXgxKy85QnJtVA==|fd8f2752362122b6aad8fdeec6744e89e3ed7c291599eab625fa961048ef6851\"; captcha_ticket_v2=\"2|1:0|10:1624439488|17:captcha_ticket_v2|704:eyJ2YWxpZGF0ZSI6IkNOMzFfOGMxbDJZdlB4Qko3VmhTWnh1Szk0OXlYanpJajF4WngyOE92Z3lPc0xBU3phOXcxMFdBQ1hZRF9mNXJIdTBjQkViYUZxWTlqdUc1Y050WmZKZjdMMXJNRHA2TnlGUkR1LUo0bVRjY0QyR0x4UC01S3c1OV9zMDhwQVhOcG5wTWF4ZHFtUmdpTldDRkRxeXdsbUpEUHBtTkFZaEFfWVB3MEotOWZhSWllZEhaNmlfcWlZc1p4N2dfbjQ5ME1oWVRhbDhQX0FXbXVjS0hiR2VIbEE2aFVVcGhGUVFmdWpDMkVaZXZTX0lXZzY3TmcxY3NIcFRseVV0bnN2ak0wWmdsRGYuS3pyWkZCX2twMlE1eDBJUWlMREFlUnNydVF6X2tIRkNsblh3aUZWRFZwNWxvUjBlTnNNTGpIQkpWNDFzLl9mLXcyaVVCV2dkemdBaVQ1WS50dGItV01UMUhxbjlSSUguWmNTcFlfN3IyVzE4ZEpOam5CVVJkODh6dC5RQzFxenE5SW5VWVNDY2s5Z190ZmJsT3dMWWVqRGJvdDFualpXZWNOLUNkMEJZNXZjSmpaWnVPNjdXQ0pVOVREREFud1ZscXE0UndqTWd0Y0NFRGNsT3MtWVlGUmhyb1JVRkdKRWV5WDJxb3Q2ZUdjYnVkUVN5aTRtalFqOXpYMyJ9|0ec8ef377ee01e06aad6600839b0ae834d3a6be51817ee7336f74b7983fc05b5\"; z_c0=\"2|1:0|10:1624439492|4:z_c0|92:Mi4xVG5jeEJRQUFBQUFBOEp5bGlrM3lFaVlBQUFCZ0FsVk54RWpBWVFBQ05Qbk5ZOGdpYld0Y2dkVTlLaENYZkQtVEtn|272351efe8ce3ce2b4c2470ca63d5193a2e06e3cf1bc506851b95ed96d101b45\"; KLBRSID=031b5396d5ab406499e2ac6fe1bb1a43|1624440938|1624439440";

    String cookie = "_zap=2ca50f0c-b83d-44ef-997d-8d2676fc2590; _xsrf=JY6DNS65LhUWFLk2i0C1RwPBp5FjJ3kR; ISSW=1; d_c0=\"AOCcZ9MnThOPTtspPSA9iCwSdvINf3DNPWg=|1624442445\"; Hm_lvt_98beee57fd2ef70ccdd5ca52b9740c49=1622694294,1623718869,1624323707,1624442447; captcha_session_v2=\"2|1:0|10:1624442446|18:captcha_session_v2|88:MEN5NElnSWtDQUc2SFlxaTFkMjd3ZzhHcXJ5aHM4NFZoR09tbEdXcXdsNWJMeGVqdXlBVlQ1clBWc2NyQXlrcA==|485e8281e3697eaffa46f46edcd9c44f1f6a07daddbd02cdaf2f71252afe53d4\"; SESSIONID=8P1BNIcnAOhS5TDODbj65lgtRbip8nia6xclsLOoMiH; JOID=UlwRBkNJPWQ_qfKBWUwhvs01un9PdW1UcO-_4jYAdgxtka7BFgI6u1ql9oBVtH0wH1h-jBhBWTLd0r4sLkQf330=; osd=VF0XB0xPPGI-pvSAX00uuMwzu3BJdGtVf-m-5DcPcA1rkKHHFwQ7tFyk8IFasnw2Hld4jR5AVjTc1L8jKEUZ3nI=; __snaker__id=rEfQskRlNA2LnQhd; gdxidpyhxdE=Bwr9nH0OBpgLTeBwrkOGYrcBfwYpgjlQQuC2OYYKaP0NV1Eh9R0BEsCZiV%2FtN%2FiqzMBVUD98Bb%2BXC90tAWlZXomKfQOybSZqGScSBXTYuioMyuL38mYKL3rfyRvSA%5CiIa%2BuAYu5JK7mokhLYl0ZcJzg2NBZ%5CHWyOgiLseDbODuQohgIk%3A1624443349222; _9755xjdesxxd_=32; YD00517437729195%3AWM_TID=UnX%2Fmr40r1dBERABEBJ6lbWIUrenrxqz; captcha_ticket_v2=\"2|1:0|10:1624442456|17:captcha_ticket_v2|704:eyJ2YWxpZGF0ZSI6IkNOMzFfdVVEVVQ1Ll9LX0J1OGo0dWxhYkliSTRUQU54aFBpRUV0UldfQUJVQ0QxMUdxaWJMNEtZSnE4MU44ekl0YW9NLkRtLnQxS3dXSmVPbE0uUlNmY1NWWnVOYU1IVDB3UGZ3Q2hJLW5OR0N4ZFRRTWhxbW0wcXAuMkEucFhiaXRlNF9NU0d1UW5FTmYyS0JlMDdjVkt3S3dsemlrNUROT0lCYVVnQ201amUxVktVbURNZ0M1cWFLLW50MFNXam43MFdYaTJNNzZoRVBTQjcxVnRSeWhacWJkVkdrWmNnMlhzOUQ3emNHZUc5bWJhTm9TbnhyRGFLcnU5Q05oblRhMWdkVm1fa1lhSjZ4ekZyZDdJa3JlUUtLZUdpNE4tY3Z0Snhtd04yd18xNGRvSWEwdGs5Lmx5cEhpQ3ZXb2lIdzVoSl9lank1eU15WDR1aGZrMjVnU09ubFZQNGNDbHN4ZzBXX1EuQXk1ZzhaSzlSd0F1TS1sZk01emF1YnhHYS5valZXcm03SGtRRG0uZDVTNm8ucmFKVnVHRmp4YTJDa0NCalBLQUQubnFzMGxra0gwLTV6SjhHLThiVnFhNTZMa19vckZmSEs3N2RSWWNkc00uLlpVejFZTmNEVWg2d0RHb3NTV2h4YS42el8wckthdnoxa1phOHFlVndKZy5JMyJ9|048b80b7d451a90ee60cf0eee21988d0989695b15c80138c768d417ff4dba4e2\"; z_c0=\"2|1:0|10:1624442456|4:z_c0|92:Mi4xVG5jeEJRQUFBQUFBNEp4bjB5ZE9FeVlBQUFCZ0FsVk5XRlRBWVFDdFdRdVdFQ3RDX2E1bGx4a2lJdDgzREgxajVn|7d174d01486ed018ce953f92cf39d1b4078c20e08f34bc7389ae6c27ed16e6d9\"; tst=r; Hm_lpvt_98beee57fd2ef70ccdd5ca52b9740c49=1624442459; KLBRSID=fe78dd346df712f9c4f126150949b853|1624442461|1624442444";

    String cookie2 = "KLBRSID=fe78dd346df712f9c4f126150949b853|1624442461|1624442444";
    hashMap.put("cookie", cookie);

    Headers headers = Headers
        .of(hashMap);

    Request request = new Builder().url(url).get().
        headers(headers).build();
    String string = client.newCall(request).execute().body().string();

    System.out.println(string);


  }

}
