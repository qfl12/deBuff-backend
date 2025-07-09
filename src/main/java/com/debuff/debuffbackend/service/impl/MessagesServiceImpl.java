package com.debuff.debuffbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debuff.debuffbackend.entity.Messages;
import com.debuff.debuffbackend.service.MessagesService;
import com.debuff.debuffbackend.mapper.MessagesMapper;
import org.springframework.stereotype.Service;

/**
* @author m1822
* @description 针对表【messages(消息记录表)】的数据库操作Service实现
* @createDate 2025-07-09 14:55:57
*/
@Service
public class MessagesServiceImpl extends ServiceImpl<MessagesMapper, Messages>
    implements MessagesService{

}




