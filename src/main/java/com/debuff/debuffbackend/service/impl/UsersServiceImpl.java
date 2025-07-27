package com.debuff.debuffbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debuff.debuffbackend.entity.Users;
import com.debuff.debuffbackend.service.UsersService;
import com.debuff.debuffbackend.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.math.BigDecimal;

/**
* @author m1822
* {@code @description} 针对表【users(用户信息表)】的数据库操作Service实现
* {@code @createDate} 2025-07-09 14:55:57
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

    private static final Logger log = LoggerFactory.getLogger(UsersServiceImpl.class);
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
    public Users findBySteamId(String steamId) {
        log.debug("根据SteamID查询用户: {}", steamId);
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("steam_id", steamId);
        Users user = baseMapper.selectOne(queryWrapper);
        if (user == null) {
            log.debug("未找到SteamID对应的用户: {}", steamId);
        } else {
            log.debug("找到SteamID对应的用户: userId={}", user.getUserId());
        }
        return user;
    }

    /**
     * 增加用户余额
     * @param userId 用户ID
     * @param amount 增加的金额
     */
    @Override
    public void addBalance(Integer userId, BigDecimal amount) {
        log.info("开始增加用户 {} 的余额: {}", userId, amount);
        Users user = baseMapper.selectById(userId);
        if (user == null) {
            log.error("用户 {} 不存在，无法增加余额", userId);
            throw new RuntimeException("用户不存在");
        }
        if (user.getBalance() == null) {
            user.setBalance(BigDecimal.ZERO);
        }
        user.setBalance(user.getBalance().add(amount));
        baseMapper.updateById(user);
        log.info("用户 {} 余额增加成功，新余额: {}", userId, user.getBalance());
    }

    @Override
    public boolean updateById(Users entity) {
        log.debug("执行用户更新: userId={}, steamId={}", entity.getUserId(), entity.getSteamId());
        if (entity.getUserId() == null) {
            log.error("更新失败: 用户ID为null");
            return false;
        }
        int rows = baseMapper.updateById(entity);
        log.debug("用户更新影响行数: {}, SQL语句: UPDATE users SET ... WHERE user_id={}");
        return rows > 0;
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
        this.updateById(user);

        return user;
    }
}




