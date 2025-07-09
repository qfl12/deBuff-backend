package com.debuff.debuffbackend.controller;

import com.debuff.debuffbackend.entity.Items;
import com.debuff.debuffbackend.service.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商品信息表(Items)表控制层
 *
 * @author m1822
 * @since 2025-07-09 20:58:45
 */
@RestController
@RequestMapping("items")
public class ItemsController {

    /**
     * 服务对象
     */
    @Autowired
    private ItemsService itemsService;

}
