package com.debuff.debuffbackend.service;

import com.debuff.debuffbackend.entity.MarketListing;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author m1822
* @description 针对表【market_listing(用户商品挂牌表)】的数据库操作Service
* @createDate 2025-07-26 01:58:04
*/
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface MarketListingService extends IService<MarketListing> {
    /**
     * 创建商品挂牌
     * @param itemId 商品ID
     * @param userId 用户ID
     * @param price 挂牌价格
     * @param sellerNote 卖家备注
     * @return 是否创建成功
     */
    boolean createMarketListing(Long itemId, Integer userId, BigDecimal price, String sellerNote);

    /**
     * 获取用户挂牌列表
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 分页的挂牌列表
     */
    /**
     * 获取用户挂牌列表（包含商品详情）
     *
     * @param userId 用户ID
     * @param page   页码
     * @param size   每页大小
     * @return 分页的商品挂牌列表，包含商品详情信息
     */
    IPage<Map<String, Object>> getUserListings(Integer userId, int page, int size);

    /**
     * 批量检查商品是否存在ON_SALE状态的挂牌记录
     * @param userId 用户ID
     * @param itemIds 商品ID列表
     * @return 商品ID到是否存在挂牌记录的映射
     */
    Map<Long, Boolean> checkItemsHasOnSaleListing(Integer userId, List<Long> itemIds);

    /**
     * 获取随机上架的饰品
     * @param limit 获取数量
     * @return 包含饰品信息、价格、描述和卖家信息的Map列表
     */
    List<Map<String, Object>> getRandomOnSaleListings(int limit);

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
    IPage<Map<String, Object>> searchListings(int page, int size, String keyword, String type, String quality, String category, String appearance);

    /**
     * 根据ID获取商品挂牌详情
     * @param id 商品挂牌ID
     * @return 包含商品详情、价格和卖家信息的Map
     */
    Map<String, Object> getListingById(Long id);

    /**
     * 更新商品挂牌信息
     * @param listingId 挂牌ID
     * @param userId 用户ID
     * @param price 新价格
     * @param sellerNote 新备注
     * @return 是否更新成功
     */
    boolean updateMarketListing(Long listingId, Integer userId, BigDecimal price, String sellerNote);

    /**
     * 下架商品
     * @param listingId 挂牌ID
     * @param userId 用户ID
     * @return 是否下架成功
     */
    boolean deleteMarketListing(Long listingId, Integer userId);

}
