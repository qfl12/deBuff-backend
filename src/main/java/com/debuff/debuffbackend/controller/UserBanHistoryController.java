package com.debuff.debuffbackend.controller;

import com.debuff.debuffbackend.entity.UserBanHistory;
import com.debuff.debuffbackend.service.UserBanHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 记录用户的封禁历史信息(UserBanHistory)表控制层
 *
 * @author m1822
 * @since 2025-07-09 20:58:45
 */
@RestController
@RequestMapping("userBanHistory")
public class UserBanHistoryController {

    /**
     * 服务对象
     */
    @Autowired
    private UserBanHistoryService userBanHistoryService;

}
