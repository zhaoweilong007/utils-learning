package com.zwl.jsoup.model;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.IEntity;
import lombok.Data;

/**
 * 话题实体
 * @author zhao_wei_long
 * @since 2021/6/25
 **/
@Data
@FluentMybatis(table = "topic")
public class Topic implements IEntity {

  @TableId
  private Long id;

  private Long parentId;

  private Integer topicId;

  private String topicName;

  private String comment;
}
