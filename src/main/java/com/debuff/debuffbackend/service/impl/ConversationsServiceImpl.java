package com.debuff.debuffbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debuff.debuffbackend.entity.Conversations;
import com.debuff.debuffbackend.service.ConversationsService;
import com.debuff.debuffbackend.mapper.ConversationsMapper;
import org.springframework.stereotype.Service;

/**
* @author m1822
* @description 针对表【conversations(会话管理表)】的数据库操作Service实现
* @createDate 2025-07-09 14:55:57
*/
@Service
public class ConversationsServiceImpl extends ServiceImpl<ConversationsMapper, Conversations>
    implements ConversationsService{

}




