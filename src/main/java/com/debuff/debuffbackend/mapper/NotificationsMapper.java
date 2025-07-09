package com.debuff.debuffbackend.mapper;

import com.debuff.debuffbackend.entity.Notifications;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author m1822
* @description 针对表【notifications(通知信息表)】的数据库操作Mapper
* @createDate 2025-07-09 14:55:57
* @Entity com.debuff.debuffbackend.entity.Notifications
*/
@Mapper
public interface NotificationsMapper extends BaseMapper<Notifications> {

}




