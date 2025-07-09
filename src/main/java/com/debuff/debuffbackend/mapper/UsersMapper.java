package com.debuff.debuffbackend.mapper;

import com.debuff.debuffbackend.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author m1822
* @description 针对表【users(用户信息表)】的数据库操作Mapper
* @createDate 2025-07-09 14:55:57
* @Entity com.debuff.debuffbackend.entity.Users
*/
@Mapper
public interface UsersMapper extends BaseMapper<Users> {

}




