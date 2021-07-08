package com.zwl.jsoup.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zwl.jsoup.model.Answer;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Answer)表数据库访问层
 *
 * @author ZhaoWeiLong
 * @since 2021-07-08 17:59:46
 */
@Mapper
public interface AnswerMapper extends BaseMapper<Answer> {

}
