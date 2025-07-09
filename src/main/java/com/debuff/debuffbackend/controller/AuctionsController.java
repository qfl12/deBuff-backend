package com.debuff.debuffbackend.controller;

import com.debuff.debuffbackend.entity.Auctions;
import com.debuff.debuffbackend.service.AuctionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 拍卖信息表(Auctions)表控制层
 *
 * @author m1822
 * @since 2025-07-09 20:58:44
 */
@RestController
@RequestMapping("auctions")
public class AuctionsController {

    /**
     * 服务对象
     */
    @Autowired
    private AuctionsService auctionsService;

}
