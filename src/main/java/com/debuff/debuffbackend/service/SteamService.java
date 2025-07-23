package com.debuff.debuffbackend.service;

import com.debuff.util.Result;

/**
 * Steam服务接口
 * 定义与Steam API交互的方法
 */
public interface SteamService {
    /**
     * 获取Steam用户库存信息
     *
     * @param steamId Steam用户ID
     * @return 封装后的库存信息响应结果
     */
    Result<Object> getSteamInventory(String steamId);
}