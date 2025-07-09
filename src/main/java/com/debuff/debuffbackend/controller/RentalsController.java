package com.debuff.debuffbackend.controller;

import com.debuff.debuffbackend.entity.Rentals;
import com.debuff.debuffbackend.service.RentalsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 租赁信息表(Rentals)表控制层
 *
 * @author m1822
 * @since 2025-07-09 20:58:45
 */
@RestController
@RequestMapping("rentals")
public class RentalsController {

    /**
     * 服务对象
     */
    @Autowired
    private RentalsService rentalsService;

}
