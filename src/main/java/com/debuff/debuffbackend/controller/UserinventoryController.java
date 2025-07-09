package com.debuff.debuffbackend.controller;

import com.debuff.debuffbackend.entity.Userinventory;
import com.debuff.debuffbackend.service.UserinventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户库存信息表(Userinventory)表控制层
 *
 * @author m1822
 * @since 2025-07-09 20:58:46
 */
@RestController
@RequestMapping("userinventory")
public class UserinventoryController {

    /**
     * 服务对象
     */
    @Autowired
    private UserinventoryService userinventoryService;

}
