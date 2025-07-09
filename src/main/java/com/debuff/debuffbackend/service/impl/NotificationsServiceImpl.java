package com.debuff.debuffbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debuff.debuffbackend.entity.Notifications;
import com.debuff.debuffbackend.service.NotificationsService;
import com.debuff.debuffbackend.mapper.NotificationsMapper;
import org.springframework.stereotype.Service;

/**
* @author m1822
* @description 针对表【notifications(通知信息表)】的数据库操作Service实现
* @createDate 2025-07-09 14:55:57
*/
@Service
public class NotificationsServiceImpl extends ServiceImpl<NotificationsMapper, Notifications>
    implements NotificationsService{

}




