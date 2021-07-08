package com.zwl.jsoup.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 话题实体
 * @author zhao_wei_long
 * @since 2021/6/25
 **/
@Data
public class Topic{

  @TableId(type = IdType.AUTO)
  private Long id;

  private Long parentId;

  private Integer topicId;

  private String topicName;

  private String comment;
}
