package com.zwl;

import com.zwl.jsoup.WebCrawler;
import com.zwl.jsoup.mapper.AnswerMapper;
import com.zwl.jsoup.mapper.TopicMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhao_wei_long
 * @since 2021/6/25
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplicationTest {

  @Autowired
  @Qualifier("topicMapper")
  TopicMapper topicMapper;

  @Qualifier("answerMapper")
  @Autowired
  AnswerMapper answerMapper;

  @Autowired
  WebCrawler webCrawler;

  @Test
  public void test() throws Exception {
    String cookie = "_xsrf=DRlmLWekvxznmnldUtukflZOzyJpj3Xf; cap_id=\"MWRiNzI0OTE2MGZkNDUzOGFmN2MxZjkyM2NkZmZkMDg=|1624585721|b61c10c8cf27a9e3fa9484ea5367c9c3d824e7dd\"; l_cap_id=\"MWYzZTY4YTgyZmE2NGJkMTg0MWRmYWYxZDBjNTExNzk=|1624585721|447b2168f6bec06ac360d0c8186b2fd3a0c52064\"; q_c1=db02045582e34b66abe3c48b3a12e034|1624526918000|1624526918000; r_cap_id=\"NzEwZGMyMjZlYTkwNDY3MjhlYWFmMjc4ZTQ2MzcyNTI=|1624585721|9cc47b9e69c335a4cdc9ddec69bca3d5904bb81d\"; KLBRSID=975d56862ba86eb589d21e89c8d1e74e|1624937500|1624937476";
    webCrawler.start(cookie);

  }

  @Test
  public void testDB() {

    String content = "# 如何看待某剧组在南京大学拍戏遇冷，学生不围观、不追星？这一现象反映了什么？\n"
        + "\n"
        + "<a target=\"_blank\" href=\"https://www.zhihu.com/zvideo/1376118567822262272\" data-draft-node=\"block\" data-draft-type=\"link-card\" data-image=\"https://zhstatic.zhihu.com/assets/zhihu/editor/zhihu-card-default.svg\" class=\"LinkCard old LinkCard--hasImage\"><span class=\"LinkCard-backdrop\" style=\"background-image:url(https://zhstatic.zhihu.com/assets/zhihu/editor/zhihu-card-default.svg)\"></span><span class=\"LinkCard-content\"><span class=\"LinkCard-text\"><span class=\"LinkCard-title\" data-text=\"true\">知乎视频</span><span class=\"LinkCard-meta\"><span style=\"display:inline-flex;align-items:center\">\u200B\n"
        + "     <svg class=\"Zi Zi--InsertLink\" fill=\"currentColor\" viewbox=\"0 0 24 24\" width=\"17\" height=\"17\">\n"
        + "      <path d=\"M13.414 4.222a4.5 4.5 0 1 1 6.364 6.364l-3.005 3.005a.5.5 0 0 1-.707 0l-.707-.707a.5.5 0 0 1 0-.707l3.005-3.005a2.5 2.5 0 1 0-3.536-3.536l-3.005 3.005a.5.5 0 0 1-.707 0l-.707-.707a.5.5 0 0 1 0-.707l3.005-3.005zm-6.187 6.187a.5.5 0 0 1 .638-.058l.07.058.706.707a.5.5 0 0 1 .058.638l-.058.07-3.005 3.004a2.5 2.5 0 0 0 3.405 3.658l.13-.122 3.006-3.005a.5.5 0 0 1 .638-.058l.069.058.707.707a.5.5 0 0 1 .058.638l-.058.069-3.005 3.005a4.5 4.5 0 0 1-6.524-6.196l.16-.168 3.005-3.005zm8.132-3.182a.25.25 0 0 1 .353 0l1.061 1.06a.25.25 0 0 1 0 .354l-8.132 8.132a.25.25 0 0 1-.353 0l-1.061-1.06a.25.25 0 0 1 0-.354l8.132-8.132z\"></path>\n"
        + "     </svg></span>www.zhihu.com</span></span><span class=\"LinkCard-imageCell\"><img class=\"LinkCard-image LinkCard-image--square\" alt=\"图标\" src=\"https://zhstatic.zhihu.com/assets/zhihu/editor/zhihu-card-default.svg\"></span></span></a>\n"
        + "\n"
        + "<p>谁尴尬？不应该剧组尴尬吗？</p>\n"
        + "\n"
        + "<p>十年前俺在俺们大专读书的时候，给营业厅兼职一天都有150，人家还送雨伞送体恤衫啥的。</p>\n"
        + "\n"
        + "<p>你这就出75，别的啥也没有，就想让人985的学生，顶着南京的大太阳站一天，还得受你气，真把别人当傻子了是吧？</p>\n"
        + "\n"
        + "<p>顶流明星一天208万，到了学生这连一百块都没有，就算横店群演也没这么便宜吧？</p>\n"
        + "\n"
        + "<p>好久之前某剧组来山大拍戏，大中午的占着去食堂的路不让走，让学生们绕半个学校去吃饭。</p>\n"
        + "\n"
        + "<p>还有去年某剧组在南航拍戏，不让人拿外卖不让人晒衣服，大半夜还整个大灯在宿舍楼外面。</p>\n"
        + "\n"
        + "<p>这是阳间能干出来的事？</p>\n"
        + "\n"
        + "<img src=\"https://pic4.zhimg.com/50/v2-4b32951f90da798dbfeb1175eb5592f9_hd.jpg?source=1940ef5c\" data-rawwidth=\"1440\" data-rawheight=\"1920\" data-size=\"normal\" data-default-watermark-src=\"https://pic4.zhimg.com/50/v2-2455a304f7b3165c4cf298bfe8eb1226_hd.jpg?source=1940ef5c\" class=\"origin_image zh-lightbox-thumb\" width=\"1440\" data-original=\"https://pic1.zhimg.com/v2-4b32951f90da798dbfeb1175eb5592f9_r.jpg?source=1940ef5c\">\n"
        + "\n"
        + "<p>不止是我说的这两个例子。</p>\n"
        + "\n"
        + "<p>但凡是个剧组，只要进了学校，不是堵着宿舍楼，就是堵着图书馆。</p>\n"
        + "\n"
        + "<p>你们这已经影响到学生生活了好嘛？</p>\n"
        + "\n"
        + "<p>不把你们指名道姓地挂在网上已经不错了，还想用白嫖的价格请学生来参演？？</p>\n"
        + "\n"
        + "<p>更可恨的是，影响就影响了，影响完还借着南大学生的表现给自己搞营销，什么少年感，什么剧很好。</p>\n"
        + "\n"
        + "<p>你们搁这贴金呢？</p>\n"
        + "\n"
        + "<img src=\"https://pic3.zhimg.com/50/v2-038760949ce753632f049a18cdd28390_hd.jpg?source=1940ef5c\" data-rawwidth=\"1125\" data-rawheight=\"2436\" data-size=\"normal\" data-default-watermark-src=\"https://pic4.zhimg.com/50/v2-8864d869806685c337b700ebdc947889_hd.jpg?source=1940ef5c\" class=\"origin_image zh-lightbox-thumb\" width=\"1125\" data-original=\"https://pic4.zhimg.com/v2-038760949ce753632f049a18cdd28390_r.jpg?source=1940ef5c\">\n"
        + "\n"
        + "<p>讲真，你们已经影响到学校里的正常少年了好嘛？</p>\n"
        + "\n"
        + "<p>真的是有点过分了。</p>\n"
        + "\n"
        + "<p class=\"ztext-empty-paragraph\"><br></p>\n"
        + "\n"
        + "<hr>\n"
        + "\n"
        + "<p>以上，我是 <a class=\"member_mention\" href=\"https://www.zhihu.com/people/844f5a5e3c37e603af147a8f8b84ce51\" data-hash=\"844f5a5e3c37e603af147a8f8b84ce51\" data-hovercard=\"p$b$844f5a5e3c37e603af147a8f8b84ce51\">@Puddle</a> ，我们都有美好的未来。</p>\n"
        + "\n";

    System.out.println(content.length());

  }
}
