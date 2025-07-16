package com.debuff.debuffbackend.controller;

import com.debuff.debuffbackend.service.SteamService;
import com.debuff.util.Result;
import com.debuff.debuffbackend.model.SteamInventoryResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Steam控制器
 * 提供与Steam相关的API接口
 */
@RestController
@RequestMapping("/api/steam")
@Api(tags = "Steam接口")
@Slf4j
public class SteamController {

    @Autowired
    private SteamService steamService;

    /**
     * 获取Steam用户库存信息
     * @param steamId Steam用户ID
     * @return 封装后的库存信息响应结果
     */
    @GetMapping("/inventory/{steamId}")
    @ApiOperation("获取Steam用户库存")
    public Result<SteamInventoryResponse> getSteamInventory(@PathVariable String steamId) {
        log.info("前端请求获取Steam用户库存，steamId: {}", steamId);
        return steamService.getSteamInventory(steamId);
    }
}