package com.debuff.debuffbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debuff.debuffbackend.entity.Users;
import com.debuff.debuffbackend.service.UsersService;
import com.debuff.debuffbackend.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author m1822
* @description 针对表【users(用户信息表)】的数据库操作Service实现
* @createDate 2025-07-09 14:55:57
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UsersServiceImpl(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Users findByEmail(String email) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public Users login(String email, String password_hash) {
        // 根据邮箱查询用户
        Users user = findByEmail(email);
        if (user == null) {
            return null;
        }

        // 验证密码
        if (!passwordEncoder.matches(password_hash, user.getPassword())) {
            return null;
        }

        // 更新最后登录时间
        user.setLastLogin(new Date());
        baseMapper.updateById(user);

        return user;
    }
}




