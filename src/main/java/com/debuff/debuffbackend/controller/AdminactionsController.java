package com.debuff.debuffbackend.controller;

import com.debuff.debuffbackend.entity.Adminactions;
import com.debuff.debuffbackend.service.AdminactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员操作记录表(Adminactions)表控制层
 *
 * @author m1822
 * @since 2025-07-09 20:58:44
 */
@RestController
@RequestMapping("adminactions")
public class AdminactionsController {

    /**
     * 服务对象
     */
    @Autowired
    private AdminactionsService adminactionsService;

}
