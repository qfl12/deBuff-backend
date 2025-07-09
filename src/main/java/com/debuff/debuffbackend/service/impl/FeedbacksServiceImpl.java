package com.debuff.debuffbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debuff.debuffbackend.entity.Feedbacks;
import com.debuff.debuffbackend.service.FeedbacksService;
import com.debuff.debuffbackend.mapper.FeedbacksMapper;
import org.springframework.stereotype.Service;

/**
* @author m1822
* @description 针对表【feedbacks(用户反馈信息表)】的数据库操作Service实现
* @createDate 2025-07-09 14:55:57
*/
@Service
public class FeedbacksServiceImpl extends ServiceImpl<FeedbacksMapper, Feedbacks>
    implements FeedbacksService{

}




