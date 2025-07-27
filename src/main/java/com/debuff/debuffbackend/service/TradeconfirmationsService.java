package com.debuff.debuffbackend.service;

import com.debuff.debuffbackend.entity.Tradeconfirmations;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author m1822
* @description 针对表【tradeconfirmations(交易确认表)】的数据库操作Service
* @createDate 2025-07-27 00:38:37
*/
import java.util.Map;

public interface TradeconfirmationsService extends IService<Tradeconfirmations> {
    /**
     * 处理商品购买流程
     * @param itemId 商品ID
     * @param buyerId 买家ID
     * @return 交易结果
     */
    Map<String, Object> purchaseItem(Long itemId, Integer buyerId);

}
