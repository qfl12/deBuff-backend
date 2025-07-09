package com.debuff.debuffbackend.mapper;

import com.debuff.debuffbackend.entity.Messages;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author m1822
* @description 针对表【messages(消息记录表)】的数据库操作Mapper
* @createDate 2025-07-09 14:55:57
* @Entity com.debuff.debuffbackend.entity.Messages
*/
@Mapper
public interface MessagesMapper extends BaseMapper<Messages> {

}




