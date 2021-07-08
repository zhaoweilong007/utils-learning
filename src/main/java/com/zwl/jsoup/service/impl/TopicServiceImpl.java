package com.zwl.jsoup.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zwl.jsoup.mapper.TopicMapper;
import com.zwl.jsoup.model.Topic;
import com.zwl.jsoup.service.TopicService;
import org.springframework.stereotype.Service;

/**
 * (Topic)表服务实现类
 *
 * @author ZhaoWeiLong
 * @since 2021-07-08 17:59:52
 */
@Service("topicService")
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {

}
