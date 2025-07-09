package com.debuff.debuffbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debuff.debuffbackend.entity.Users;
import com.debuff.debuffbackend.service.UsersService;
import com.debuff.debuffbackend.mapper.UsersMapper;
import org.springframework.stereotype.Service;

/**
* @author m1822
* @description 针对表【users(用户信息表)】的数据库操作Service实现
* @createDate 2025-07-09 14:55:57
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

}




