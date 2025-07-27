package com.debuff.debuffbackend.service;

import com.debuff.debuffbackend.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
* @author m1822
* {@code @description} 针对表【users(用户信息表)】的数据库操作Service
* {@code @createDate} 2025-07-09 14:55:57
 */
public interface UsersService extends IService<Users> {
    /**
     * 根据邮箱查询用户
     * @param email 邮箱
     * @return 用户信息
     */
    Users findByEmail(String email);

    /**
     * 用户登录
     * @param email 邮箱
     * @param password_hash 密码
     * @return 登录成功的用户信息
     */
    Users login(String email, String password_hash);

    /**
     * 根据SteamID查询用户
     * @param steamId Steam用户ID
     * @return 用户信息
     */
    Users findBySteamId(String steamId);

    void addBalance(Integer buyerId, BigDecimal price);
}
