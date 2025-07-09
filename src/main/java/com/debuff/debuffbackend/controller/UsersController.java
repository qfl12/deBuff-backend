package com.debuff.debuffbackend.controller;

import com.debuff.debuffbackend.entity.Users;
import com.debuff.debuffbackend.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户信息表(Users)表控制层
 *
 * @author m1822
 * @since 2025-07-09 20:58:46
 */
@RestController
@RequestMapping("users")
public class UsersController {

    /**
     * 服务对象
     */
    @Autowired
    private UsersService usersService;

}
