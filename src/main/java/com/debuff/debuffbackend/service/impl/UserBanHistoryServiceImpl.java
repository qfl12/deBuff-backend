package com.debuff.debuffbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debuff.debuffbackend.entity.UserBanHistory;
import com.debuff.debuffbackend.service.UserBanHistoryService;
import com.debuff.debuffbackend.mapper.UserBanHistoryMapper;
import org.springframework.stereotype.Service;

/**
* @author m1822
* @description 针对表【user_ban_history(记录用户的封禁历史信息)】的数据库操作Service实现
* @createDate 2025-07-09 14:55:57
*/
@Service
public class UserBanHistoryServiceImpl extends ServiceImpl<UserBanHistoryMapper, UserBanHistory>
    implements UserBanHistoryService{

}




