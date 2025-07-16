package com.debuff.debuffbackend.security;

import com.debuff.debuffbackend.entity.Users;
import com.debuff.debuffbackend.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 自定义用户详情服务，用于加载用户信息
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersService usersService;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // 根据用户ID查询用户
        Users user = usersService.getById(Integer.parseInt(userId));
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 返回Spring Security需要的UserDetails对象
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserId().toString())
                .password(user.getPassword())
                .roles("USER") // 默认角色，可以根据实际情况修改
                .build();
    }
}