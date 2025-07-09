package com.debuff.debuffbackend.controller;

import com.debuff.debuffbackend.entity.Tradeconfirmations;
import com.debuff.debuffbackend.service.TradeconfirmationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 交易确认表(Tradeconfirmations)表控制层
 *
 * @author m1822
 * @since 2025-07-09 20:58:45
 */
@RestController
@RequestMapping("tradeconfirmations")
public class TradeconfirmationsController {

    /**
     * 服务对象
     */
    @Autowired
    private TradeconfirmationsService tradeconfirmationsService;

}
