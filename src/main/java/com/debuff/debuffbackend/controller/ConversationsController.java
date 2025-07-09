package com.debuff.debuffbackend.controller;

import com.debuff.debuffbackend.entity.Conversations;
import com.debuff.debuffbackend.service.ConversationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 会话管理表(Conversations)表控制层
 *
 * @author m1822
 * @since 2025-07-09 20:58:45
 */
@RestController
@RequestMapping("conversations")
public class ConversationsController {

    /**
     * 服务对象
     */
    @Autowired
    private ConversationsService conversationsService;

}
