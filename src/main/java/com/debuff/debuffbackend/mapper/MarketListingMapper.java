package com.debuff.debuffbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.debuff.debuffbackend.entity.MarketListing;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Map;

@Mapper
public interface MarketListingMapper extends BaseMapper<MarketListing> {
    /**
     * 分页搜索商品挂牌信息
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页结果，以id为键的Map集合
     */
    @MapKey("id")
    IPage<Map<String, Object>> selectSearchListingsPage(Page<MarketListing> page, Map<String, Object> params);

    /**
     * 分页查询用户挂牌商品及关联的商品详情
     * @param page 分页参数
     * @param userId 用户ID
     * @return 包含商品详情的分页结果
     */
    IPage<Map<String, Object>> selectUserListingsWithItems(Page<Map<String, Object>> page, @Param("userId") Integer userId);

    /**
     * 更新商品挂牌信息
     * @param listingId 挂牌ID
     * @param userId 用户ID
     * @param price 新价格
     * @param sellerNote 新备注
     * @return 影响行数
     */
    int updateListing(
            @Param("listingId") Long listingId,
            @Param("userId") Integer userId,
            @Param("price") BigDecimal price,
            @Param("sellerNote") String sellerNote);

    /**
     * 下架商品
     * @param listingId 挂牌ID
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteListing(
            @Param("listingId") Long listingId,
            @Param("userId") Integer userId);
    /**
     * 查询商品详情
     * @param itemId 商品ID
     * @return 商品详情
     */
    Map<String, Object> selectItemById(Long itemId);
    MarketListing selectByItemId(Object itemId);
}




