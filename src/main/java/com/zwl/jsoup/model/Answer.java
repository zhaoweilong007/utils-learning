package com.zwl.jsoup.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhao_wei_long
 * @since 2021/6/29
 **/
@Data
@Accessors(chain = true)
public class Answer implements Serializable {

  @TableId(type = IdType.AUTO)
  private Long id;

  /**
   * 话题id
   */
  private Integer topicId;

  /**
   * 回答id
   */
  private Long answerId;

  /**
   * 问题id
   */
  private Long questionId;

  /**
   * 回答url
   */
  private String answerUrl;

  /**
   * 问题
   */
  private String question;

  /**
   * 点赞数
   */
  private Integer voteupCount;

  /**
   * 回答片段
   */
  private String excerpt;

  /**
   * 作者名称
   */
  private String authorName;

  /**
   * 创建时间
   */
  private Date createDate;


  /**
   * 是否是神回复 0否 1是
   */
  private Boolean isGodReplies;

  /**
   * 回答内容
   */
  private String content;

}
