package com.debuff.debuffbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.debuff.debuffbackend.entity.Tradeconfirmations;

import java.util.List;
import java.util.Map;

public interface TradeconfirmationsService extends IService<Tradeconfirmations> {
    /**
     * 处理商品购买流程
     * @param itemId 商品ID
     * @param buyerId 买家ID
     * @return 交易结果
     */
    Map<String, Object> purchaseItem(Long itemId, Integer buyerId);

    /**
     * 查询用户交易记录
     * @param userId 用户ID
     * @param status 交易状态: TRADING-交易中, COMPLETED-交易完成, ALL-全部
     * @return 交易记录列表
     */
    List<Tradeconfirmations> getUserTradeRecords(Integer userId, String status);

}
