package com.debuff.debuffbackend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.debuff.debuffbackend.common.Result;
import com.debuff.debuffbackend.dto.MarketListingDTO;
import com.debuff.debuffbackend.entity.MarketListing;
import com.debuff.debuffbackend.service.MarketListingService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品挂牌控制器
 * 处理商品上架、查询用户挂牌等相关HTTP请求
 */
@RestController
@RequestMapping("/api/market/listings")
@RequiredArgsConstructor
@Slf4j
public class MarketListingController {

    private final MarketListingService marketListingService;

    /**
     * 批量检查商品是否存在ON_SALE状态的挂牌记录
     * @param itemIds 商品ID列表
     * @return 包含商品ID和对应挂牌状态的映射
     */
    @PostMapping("/check-on-sale")
    public ResponseEntity<Map<Long, Boolean>> checkItemsHasOnSaleListing(@RequestBody List<Long> itemIds) {
        log.info("批量检查商品挂牌状态请求，商品ID列表: {}", itemIds);
        try {
            // 获取当前登录用户ID
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long userId = Long.valueOf(authentication.getName());
            Map<Long, Boolean> result = marketListingService.checkItemsHasOnSaleListing(userId.intValue(), itemIds);
            log.info("批量检查商品挂牌状态完成，结果: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("批量检查商品挂牌状态失败", e);
            throw e;
        }
    }

    /**
     * 创建商品挂牌
     * @return 操作结果
     */
    @PostMapping("/{userId}")
    @ApiOperation("创建商品挂牌")
    public Result createListing(
            @Valid @RequestBody MarketListingDTO listingDTO,
            @PathVariable Integer userId) {

        log.info("用户{}请求创建商品挂牌，商品ID: {}, 价格: {}", userId, listingDTO.getItemId(), listingDTO.getPrice());

        boolean success = marketListingService.createMarketListing(
            listingDTO.getItemId(),
            userId,
            listingDTO.getPrice(),
            listingDTO.getSellerNote());
        if (success) {
            return Result.success("商品挂牌创建成功");
        } else {
            return Result.fail("商品挂牌创建失败，请稍后重试");
        }
    }

    /**
     * 获取用户商品挂牌列表
     * @param page 页码
     * @param size 每页大小
     * @return 分页的挂牌列表
     */
    @GetMapping
    @ApiOperation("获取用户商品挂牌列表")
    public Result<IPage<MarketListing>> getUserListings(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestAttribute("userId") Integer userId) {

        log.info("用户{}请求获取商品挂牌列表，页码: {}, 每页大小: {}", userId, page, size);
        IPage<MarketListing> listings = marketListingService.getUserListings(userId, page, size);
        return Result.success(listings);
    }

    /**
     * 获取随机上架的饰品
     * @param limit 获取数量，默认6个（两列，每列3个）
     * @return 随机上架的饰品列表
     */
    @GetMapping("/random-on-sale")
    public ResponseEntity<List<Map<String, Object>>> getRandomOnSaleListings(@RequestParam(defaultValue = "6") int limit) {
        log.info("获取随机上架饰品请求，数量: {}", limit);
        try {
            List<Map<String, Object>> result = marketListingService.getRandomOnSaleListings(limit);
            log.info("获取随机上架饰品成功，返回数量: {}", result.size());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("获取随机上架饰品失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    /**
     * 分页搜索商品挂牌
     * @param page 页码
     * @param size 每页大小
     * @param keyword 搜索关键词
     * @param type 商品类型
     * @param quality 商品品质
     * @param category 商品类别
     * @param appearance 商品外观
     * @return 分页的商品列表
     */
    @GetMapping("/search")
    public ResponseEntity<IPage<Map<String, Object>>> searchListings(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String quality,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String appearance) {
        log.info("分页搜索商品请求，页码: {}, 每页大小: {}, 关键词: {}, 类型: {}, 品质: {}, 类别: {}, 外观: {}",
                page, size, keyword, type, quality, category, appearance);
        try {
            IPage<Map<String, Object>> result = marketListingService.searchListings(page, size, keyword, type, quality, category, appearance);
            log.info("分页搜索商品成功，总记录数: {}", result.getTotal());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("分页搜索商品失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 根据ID获取商品挂牌详情
     * @param id 商品挂牌ID
     * @return 商品详情信息
     */
    @GetMapping("/check-on-sale/{id}")
    public ResponseEntity<Map<String, Object>> getListingById(@PathVariable Long id) {
        log.info("获取商品挂牌详情请求，ID: {}", id);
        try {
            Map<String, Object> result = marketListingService.getListingById(id);
            if (result == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("获取商品挂牌详情失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}