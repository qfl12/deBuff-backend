package com.debuff.debuffbackend.controller;

import com.debuff.debuffbackend.entity.Orders;
import com.debuff.debuffbackend.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单信息表(Orders)表控制层
 *
 * @author m1822
 * @since 2025-07-09 20:58:45
 */
@RestController
@RequestMapping("orders")
public class OrdersController {

    /**
     * 服务对象
     */
    @Autowired
    private OrdersService ordersService;

}
