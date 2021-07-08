package com.zwl.jsoup.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zwl.jsoup.model.Topic;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Topic)表数据库访问层
 *
 * @author ZhaoWeiLong
 * @since 2021-07-08 17:59:51
 */
@Mapper
public interface TopicMapper extends BaseMapper<Topic> {

}
