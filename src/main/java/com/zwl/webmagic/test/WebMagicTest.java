package com.zwl.webmagic.test;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述： webMagic测试 爬取博客园练练手
 *
 * @author zwl
 * @since 2022/5/19 16:53
 */
@Slf4j
public class WebMagicTest implements PageProcessor {

  private final Site site =
      Site.me()
          .setDomain("cnblogs.com")
          .setCharset("utf-8")
          .setSleepTime(1000)
          .setRetryTimes(3)
          .setTimeOut(3000);

  private static final List<Blog> BLOGS = new ArrayList<>();

  private Integer pageTotal;

  private Integer currentPage = 1;

  @Override
  public void process(Page page) {
    List<Selectable> nodes =
        page.getHtml()
            .xpath(
                "/html/body/div[@id='wrapper']/div[@id='main_content']/div[@id='main']/div[@id='main_flow']/div[@class='card']/div[@id='post_list']/article[@class='post-item']")
            .nodes();

    for (Selectable node : nodes) {
      String title =
          node.xpath(
                  "/article/section[@class='post-item-body']/div[@class='post-item-text']/a/text()")
              .toString();

      String url =
          node.xpath(
                  "/article/section[@class='post-item-body']/div[@class='post-item-text']/a/@href")
              .toString();

      String author =
          node.xpath("/article/section[@class='post-item-body']/footer/a/span/text()").toString();

      String date =
          node.xpath("/article/section[@class='post-item-body']/footer/span/span/text()")
              .toString();

      log.info("title:{},url:{},author:{},date:{}", title, url, author, date);

      page.putField("title", title);
      page.putField("url", url);
      page.putField("author", author);
      page.putField("date", date);

      Blog blog = new Blog().setTitle(title).setUrl(url).setAuthor(author).setDate(date);
      BLOGS.add(blog);

      if (pageTotal == null) {
        String pageTotalStr =
            page.getHtml()
                .xpath(
                    "/html/body/div[@id='wrapper']/div[@id='main_content']/div[@id='main']/div[@id='main_flow']/div[@class='card']/div[@id='pager_bottom']/div[@id='paging_block']/div[@class='pager']/a[@class='p_82 last']/text()")
                .toString();

        pageTotal = Integer.parseInt(pageTotalStr);
      }

      if (++currentPage < pageTotal) {
        page.addTargetRequest("https://www.cnblogs.com/pick/" + currentPage);
      }
    }
  }

  @Override
  public Site getSite() {
    return site;
  }

  @Data
  @Accessors(chain = true)
  static class Blog {
    String title;
    String url;
    String author;
    String date;
  }

  public static void main(String[] args) throws IOException {

    String path = ResourceUtils.getURL("classpath:").getPath();

    Spider.create(new WebMagicTest())
        .thread(12)
        .addUrl("https://www.cnblogs.com/pick")
        .addPipeline(new ConsolePipeline())
        .run();

    String json = JSON.toJSONString(BLOGS, true);
    File file = new File(path + "/blog.json");
    file.createNewFile();
    FileUtil.writeString(json, file, "utf-8");
  }
}
