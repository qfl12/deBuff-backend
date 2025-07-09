package com.debuff.debuffbackend.controller;

import com.debuff.debuffbackend.entity.Transactions;
import com.debuff.debuffbackend.service.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 交易记录表(Transactions)表控制层
 *
 * @author m1822
 * @since 2025-07-09 20:58:45
 */
@RestController
@RequestMapping("transactions")
public class TransactionsController {

    /**
     * 服务对象
     */
    @Autowired
    private TransactionsService transactionsService;

}
