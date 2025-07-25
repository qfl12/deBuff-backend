package com.debuff.debuffbackend.service;

import com.debuff.debuffbackend.common.Result;
import com.debuff.debuffbackend.entity.Items;

import java.util.List;

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
    Result<List<Items>> getSteamInventory(String steamId);
}