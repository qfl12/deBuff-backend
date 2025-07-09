package com.debuff.debuffbackend.controller;

import com.debuff.debuffbackend.entity.Messages;
import com.debuff.debuffbackend.service.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 消息记录表(Messages)表控制层
 *
 * @author m1822
 * @since 2025-07-09 20:58:45
 */
@RestController
@RequestMapping("messages")
public class MessagesController {

    /**
     * 服务对象
     */
    @Autowired
    private MessagesService messagesService;

}
