package com.debuff.debuffbackend.controller;

import com.debuff.debuffbackend.entity.Feedbacks;
import com.debuff.debuffbackend.service.FeedbacksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户反馈信息表(Feedbacks)表控制层
 *
 * @author m1822
 * @since 2025-07-09 20:58:45
 */
@RestController
@RequestMapping("feedbacks")
public class FeedbacksController {

    /**
     * 服务对象
     */
    @Autowired
    private FeedbacksService feedbacksService;

}
