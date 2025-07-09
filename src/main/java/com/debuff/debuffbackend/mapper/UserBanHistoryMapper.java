package com.debuff.debuffbackend.mapper;

import com.debuff.debuffbackend.entity.UserBanHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author m1822
* @description 针对表【user_ban_history(记录用户的封禁历史信息)】的数据库操作Mapper
* @createDate 2025-07-09 14:55:57
* @Entity com.debuff.debuffbackend.entity.UserBanHistory
*/
@Mapper
public interface UserBanHistoryMapper extends BaseMapper<UserBanHistory> {

}




