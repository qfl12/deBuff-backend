package com.debuff.debuffbackend.controller;

import com.debuff.debuffbackend.entity.Notifications;
import com.debuff.debuffbackend.service.NotificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 通知信息表(Notifications)表控制层
 *
 * @author m1822
 * @since 2025-07-09 20:58:45
 */
@RestController
@RequestMapping("notifications")
public class NotificationsController {

    /**
     * 服务对象
     */
    @Autowired
    private NotificationsService notificationsService;

}
