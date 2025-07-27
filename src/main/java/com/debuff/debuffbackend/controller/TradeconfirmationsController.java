package com.debuff.debuffbackend.controller;

import com.debuff.debuffbackend.service.TradeconfirmationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 交易确认表(Tradeconfirmations)表控制层
 *
 * @author m1822
 * @since 2025-07-09 20:58:45
 */
@RestController
@RequestMapping("/api/tradeconfirmations")
public class TradeconfirmationsController {

    private static final Logger log = LoggerFactory.getLogger(TradeconfirmationsController.class);

    /**
     * 服务对象
     */
    @Autowired
    private TradeconfirmationsService tradeconfirmationsService;

    /**
     * 处理商品购买请求
     */
    @PostMapping("/purchase")
    public ResponseEntity<Map<String, Object>> purchaseItem(@RequestBody Map<String, Object> request) {
        // 验证请求参数
        if (request.get("itemId") == null || request.get("buyerId") == null) {
            log.error("商品购买请求参数缺失 - itemId: {}, buyerId: {}", request.get("itemId"), request.get("buyerId"));
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "请求参数缺失: itemId和buyerId为必填项"
            ));
        }

        Long itemId;
        Integer buyerId;
        try {
            itemId = Long.valueOf(request.get("itemId").toString());
            buyerId = Integer.valueOf(request.get("buyerId").toString());
            log.info("收到商品购买请求 - 商品ID: {}, 买家ID: {}", itemId, buyerId);

            Map<String, Object> result = tradeconfirmationsService.purchaseItem(itemId, buyerId);
            log.info("商品购买请求处理完成 - 结果: {}", result);
            return ResponseEntity.ok(result);
        } catch (NumberFormatException e) {
            log.error("商品购买请求参数格式错误 - itemId: {}, buyerId: {}", request.get("itemId"), request.get("buyerId"), e);
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "参数格式错误: itemId和buyerId必须为数字"
            ));
        } catch (Exception e) {
            log.error("商品购买处理失败", e);
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "购买处理失败: " + e.getMessage()
            ));
        }
    }
}
