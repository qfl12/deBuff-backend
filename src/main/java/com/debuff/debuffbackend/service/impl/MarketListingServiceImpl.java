package com.debuff.debuffbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debuff.debuffbackend.entity.Items;
import com.debuff.debuffbackend.entity.MarketListing;
import com.debuff.debuffbackend.entity.Users;
import com.debuff.debuffbackend.exception.BusinessException;
import com.debuff.debuffbackend.mapper.ItemsMapper;
import com.debuff.debuffbackend.mapper.MarketListingMapper;
import com.debuff.debuffbackend.mapper.UsersMapper;
import com.debuff.debuffbackend.service.MarketListingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MarketListingServiceImpl extends ServiceImpl<MarketListingMapper, MarketListing>
    implements MarketListingService{

    @Autowired
    private ItemsMapper itemsMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private MarketListingMapper marketListingMapper;

    @Override
    /**
     * 创建商品挂牌
     * @param itemId 商品ID
     * @param userId 用户ID
     * @param price 价格
     * @param sellerNote 卖家备注
     * @return 是否创建成功
     */
    public boolean createMarketListing(Long itemId, Integer userId, BigDecimal price, String sellerNote) {
        log.info("用户{}开始创建商品挂牌，商品ID: {}, 价格: {}", userId, itemId, price);
        try {
            // 检查该商品是否已存在未售出的挂牌
            QueryWrapper<MarketListing> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("item_id", itemId)
                       .eq("status", "ON_SALE");
            MarketListing existingListing = baseMapper.selectOne(queryWrapper);
            if (existingListing != null) {
                String errorMsg = "商品ID: " + itemId + "已存在未售出的挂牌记录";
                log.warn("用户{}商品挂牌创建失败，{}", userId, errorMsg);
                throw new BusinessException(errorMsg);
            }
            MarketListing listing = new MarketListing();
            listing.setItemId(itemId);
            listing.setUserId(userId);
            listing.setPrice(price);
            listing.setSellerNote(sellerNote);
            listing.setStatus("ON_SALE"); // 设置初始状态为挂牌中
            listing.setCreatedAt(new Date());
            listing.setUpdatedAt(new Date());

            boolean result = save(listing);
            if (result) {
                log.info("用户{}商品挂牌创建成功，挂牌ID: {}", userId, listing.getId());
            } else {
                log.error("用户{}商品挂牌创建失败", userId);
            }
            return result;
        } catch (BusinessException e) {
            // 业务异常直接抛出，由控制器统一处理
            throw e;
        } catch (Exception e) {
            log.error("用户{}创建商品挂牌系统异常: {}", userId, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 批量检查商品是否存在ON_SALE状态的挂牌记录
     * @param userId 用户ID
     * @param itemIds 商品ID列表
     * @return 商品ID到是否存在挂牌记录的映射
     */
    @Override
    public Map<Long, Boolean> checkItemsHasOnSaleListing(Integer userId, List<Long> itemIds) {
        log.info("用户{}批量检查商品挂牌状态，商品ID列表: {}", userId, itemIds);
        try {
            if (itemIds == null || itemIds.isEmpty()) {
                log.info("用户{}批量检查商品挂牌状态，商品ID列表为空", userId);
                return new HashMap<>();
            }

            QueryWrapper<MarketListing> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                       .in("item_id", itemIds)
                       .eq("status", "ON_SALE")
                       .select("item_id");

            List<MarketListing> listings = baseMapper.selectList(queryWrapper);
            Set<Long> listedItemIds = listings.stream()
                                              .map(MarketListing::getItemId)
                                              .collect(Collectors.toSet());

            Map<Long, Boolean> resultMap = new HashMap<>();
            for (Long itemId : itemIds) {
                resultMap.put(itemId, listedItemIds.contains(itemId));
            }

            log.info("用户{}批量检查商品挂牌状态完成，结果: {}", userId, resultMap);
            return resultMap;
        } catch (Exception e) {
            log.error("用户{}批量检查商品挂牌状态异常: {}", userId, e.getMessage(), e);
            return new HashMap<>();
        }
    }

    /**
     * 查询用户商品挂牌列表，同时关联查询商品详情
     *
     * @param userId 用户ID
     * @param page   页码
     * @param size   每页大小
     * @return 包含商品详情的分页列表
     */
    @Override
    public IPage<Map<String, Object>> getUserListings(Integer userId, int page, int size) {
        log.info("用户{}查询商品挂牌列表（含商品信息），页码: {}, 每页大小: {}", userId, page, size);
        try {
            Page<Map<String, Object>> pagination = new Page<>(page, size);
            // 关联查询market_listing和item表，获取商品详情
            IPage<Map<String, Object>> result = baseMapper.selectUserListingsWithItems(pagination, userId);
            log.info("用户{}查询商品挂牌列表成功，共{}条记录", userId, result.getTotal());
            return result;
        } catch (Exception e) {
            log.error("用户{}查询商品挂牌列表异常: {}", userId, e.getMessage(), e);
            return new Page<>();
        }
    }

    /**
     * 根据ID获取商品挂牌详情
     * @param id 商品挂牌ID
     * @return 包含商品详情、价格和卖家信息的Map
     */
    @Override
    public Map<String, Object> getListingById(Long id) {
        log.info("获取商品挂牌详情，ID: {}", id);
        try {
            // 查询挂牌记录
            MarketListing listing = baseMapper.selectById(id);
            if (listing == null) {
                log.info("未找到ID为{}的商品挂牌记录", id);
                return null;
            }

            // 查询商品信息
            Items item = itemsMapper.selectById(listing.getItemId());
            if (item == null) {
                log.info("商品ID{}不存在", listing.getItemId());
                return null;
            }

            // 查询卖家信息
            Users seller = usersMapper.selectById(listing.getUserId());

            // 组装结果
            Map<String, Object> result = new HashMap<>();
            result.put("id", item.getId());
            result.put("name", item.getName());
            result.put("price", listing.getPrice());
            result.put("imageUrl", item.getIconUrl());
            result.put("type", item.getType());
            result.put("marketName", item.getMarketName());
            result.put("listingId", listing.getId());
            result.put("description", item.getDescription());
            result.put("sellerNote", listing.getSellerNote());
            result.put("sellerUsername", seller != null ? seller.getUsername() : "未知卖家");
            result.put("sellerUserId", seller != null ? seller.getUserId() : 1 );
            result.put("sellerAvatarUrl", seller!= null? seller.getAvatarUrl() : "/avatars/default_avatar.jpg");
            result.put("status", listing.getStatus());
            result.put("createdAt", listing.getCreatedAt());

            return result;
        } catch (Exception e) {
            log.error("获取商品挂牌详情异常", e);
            return null;
        }
    }

    @Override
    public boolean updateMarketListing(Long listingId, Integer userId, BigDecimal price, String sellerNote) {
        log.info("用户{}更新商品挂牌，挂牌ID: {}, 新价格: {}", userId, listingId, price);
        try {
            // 验证参数
            if (listingId == null || userId == null || price == null) {
                String errorMsg = "参数错误，挂牌ID、用户ID和价格不能为空";
                log.warn(errorMsg);
                throw new BusinessException(errorMsg);
            }

            // 检查挂牌是否存在且属于当前用户
            MarketListing listing = baseMapper.selectById(listingId);
            if (listing == null) {
                String errorMsg = "商品挂牌不存在，ID: " + listingId;
                log.warn("用户{}更新商品挂牌失败，{}", userId, errorMsg);
                throw new BusinessException(errorMsg);
            }
            if (!listing.getUserId().equals(userId)) {
                String errorMsg = "没有权限操作此商品挂牌";
                log.warn("用户{}更新商品挂牌失败，{}", userId, errorMsg);
                throw new BusinessException(errorMsg);
            }
            if (!"ON_SALE".equals(listing.getStatus())) {
                String errorMsg = "只有状态为'ON_SALE'的商品可以编辑";
                log.warn("用户{}更新商品挂牌失败，{}", userId, errorMsg);
                throw new BusinessException(errorMsg);
            }

            // 更新商品信息
            listing.setPrice(price);
            listing.setSellerNote(sellerNote);
            listing.setUpdatedAt(new Date());
            boolean result = updateById(listing);
            if (result) {
                log.info("用户{}更新商品挂牌成功，挂牌ID: {}", userId, listingId);
            } else {
                log.error("用户{}更新商品挂牌失败，挂牌ID: {}", userId, listingId);
            }
            return result;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("用户{}更新商品挂牌系统异常: {}", userId, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean deleteMarketListing(Long listingId, Integer userId) {
        log.info("用户{}删除商品挂牌，挂牌ID: {}", userId, listingId);
        try {
            // 验证参数
            if (listingId == null || userId == null) {
                String errorMsg = "参数错误，挂牌ID和用户ID不能为空";
                log.warn(errorMsg);
                throw new BusinessException(errorMsg);
            }

            // 检查挂牌是否存在且属于当前用户
            MarketListing listing = baseMapper.selectById(listingId);
            if (listing == null) {
                String errorMsg = "商品挂牌不存在，ID: " + listingId;
                log.warn("用户{}删除商品挂牌失败，{}", userId, errorMsg);
                throw new BusinessException(errorMsg);
            }
            if (!listing.getUserId().equals(userId)) {
                String errorMsg = "没有权限操作此商品挂牌";
                log.warn("用户{}删除商品挂牌失败，{}", userId, errorMsg);
                throw new BusinessException(errorMsg);
            }

            // 执行删除操作
            int affectedRows = marketListingMapper.deleteListing(listingId, userId);
            boolean result = affectedRows > 0;
            
            if (result) {
                log.info("用户{}删除商品挂牌成功，挂牌ID: {}", userId, listingId);
            } else {
                log.error("用户{}删除商品挂牌失败，挂牌ID: {}", userId, listingId);
            }
            return result;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("用户{}删除商品挂牌系统异常: {}", userId, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 获取随机上架的饰品
     * @param limit 获取数量
     * @return 包含饰品信息、价格、描述和卖家信息的Map列表
     */
    @Override
    public List<Map<String, Object>> getRandomOnSaleListings(int limit) {
        log.info("获取随机上架饰品，数量: {}", limit);
        try {
            // 查询所有ON_SALE状态的挂牌记录并随机排序
            QueryWrapper<MarketListing> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status", "ON_SALE")
                       .orderByAsc("RAND()")
                       .last("LIMIT " + limit);

            List<MarketListing> listings = baseMapper.selectList(queryWrapper);
            if (listings.isEmpty()) {
                log.info("没有找到上架的饰品");
                return new ArrayList<>();
            }

            // 获取所有商品ID和用户ID
            List<Long> itemIds = listings.stream()
                                        .map(MarketListing::getItemId)
                                        .collect(Collectors.toList());
            List<Integer> userIds = listings.stream()
                                           .map(MarketListing::getUserId)
                                           .collect(Collectors.toList());

            // 查询商品信息
            QueryWrapper<Items> itemsQueryWrapper = new QueryWrapper<>();
            itemsQueryWrapper.in("id", itemIds);
            List<Items> items = itemsMapper.selectList(itemsQueryWrapper);
            Map<Long, Items> itemMap = items.stream()
                                           .collect(Collectors.toMap(Items::getId, item -> item));

            // 查询用户信息
            QueryWrapper<Users> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.in("user_id", userIds);
            List<Users> users = usersMapper.selectList(userQueryWrapper);
            Map<Integer, String> userMap = users.stream()
                                          .collect(Collectors.toMap(Users::getUserId, Users::getUsername));

            // 组装结果
            List<Map<String, Object>> result = new ArrayList<>();
            for (MarketListing listing : listings) {
                Items item = itemMap.get(listing.getItemId());
                if (item != null) {
                    Map<String, Object> itemInfo = new HashMap<>();
                    itemInfo.put("id", item.getId());
                    itemInfo.put("name", item.getName());
                    itemInfo.put("marketName", item.getMarketName());
                    itemInfo.put("price", listing.getPrice());
                    itemInfo.put("imageUrl", item.getIconUrl());
                    itemInfo.put("listingId", listing.getId());
                    itemInfo.put("description", item.getDescription());
                    itemInfo.put("sellerNote", listing.getSellerNote());
                    itemInfo.put("sellerUsername", userMap.getOrDefault(listing.getUserId(), "未知卖家"));
                    result.add(itemInfo);
                }
            }

            log.info("成功获取{}条随机上架饰品", result.size());
            return result;
        } catch (Exception e) {
            log.error("获取随机上架饰品异常: {}", e.getMessage(), e);
            return new ArrayList<>();
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
@Override
public IPage<Map<String, Object>> searchListings(int page, int size, String keyword,
        String type, String quality, String category, String appearance) {
    log.info("执行分页搜索商品，页码: {}, 每页大小: {}, 关键词: {}, 类型: {}, 品质: {}, 类别: {}, 外观: {}",
            page, size, keyword, type, quality, category, appearance);

    try {
        // 1. 创建分页对象
        Page<MarketListing> pagination = new Page<>(page, size);

        // 2. 构建查询条件
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("status", "ON_SALE");

        if (StringUtils.hasText(keyword)) {
            queryParams.put("keyword", "%" + keyword + "%");
        }
        if (StringUtils.hasText(type)) {
            queryParams.put("type", type);
        }
        if (StringUtils.hasText(quality)) {
            queryParams.put("quality", quality);
        }
        if (StringUtils.hasText(category)) {
            queryParams.put("category", category);
        }
        if (StringUtils.hasText(appearance)) {
            queryParams.put("appearance", appearance);
        }

        // 3. 执行查询
        IPage<Map<String, Object>> listingPage = marketListingMapper.selectSearchListingsPage(pagination, queryParams);
        log.info("查询结果: {}", listingPage.getRecords());

        // 4. 转换结果格式
        List<Map<String, Object>> records = listingPage.getRecords().stream().map(itemMap -> {
            Map<String, Object> resultMap = new LinkedHashMap<>();
            // 商品基本信息
            resultMap.put("id", itemMap.get("listing_id"));
            resultMap.put("listingId", itemMap.get("listing_id"));
            resultMap.put("itemId", itemMap.get("item_id"));
            resultMap.put("price", itemMap.get("price"));
            resultMap.put("sellerNote", itemMap.get("seller_note"));
            resultMap.put("status", itemMap.get("status"));
            resultMap.put("createdAt", itemMap.get("created_at"));
            resultMap.put("updatedAt", itemMap.get("updated_at"));

            // 商品详情
            resultMap.put("name", itemMap.get("item_name"));
            resultMap.put("marketName", itemMap.get("market_name"));
            resultMap.put("imageUrl", itemMap.get("image_url"));
            resultMap.put("description", itemMap.get("description"));
            resultMap.put("type", itemMap.get("type"));
            resultMap.put("quality", itemMap.get("quality"));
            resultMap.put("category", itemMap.get("category"));
            resultMap.put("appearance", itemMap.get("appearance"));
            resultMap.put("appid", itemMap.get("appid"));
            resultMap.put("classid", itemMap.get("classid"));
            resultMap.put("instanceid", itemMap.get("instanceid"));
            resultMap.put("marketHashName", itemMap.get("market_hash_name"));
            resultMap.put("tradable", itemMap.get("tradable"));
            resultMap.put("commodity", itemMap.get("commodity"));
            resultMap.put("marketTradableRestriction", itemMap.get("market_tradable_restriction"));
            resultMap.put("marketMarketableRestriction", itemMap.get("market_marketable_restriction"));
            resultMap.put("quantity", itemMap.get("quantity"));
            resultMap.put("acquisitionTime", itemMap.get("acquisition_time"));

            // 卖家信息
            resultMap.put("sellerId", itemMap.get("seller_id"));
            resultMap.put("sellerUsername", itemMap.get("seller_username"));

            return resultMap;
        }).collect(Collectors.toList());

            // 构建返回的分页对象
            Page<Map<String, Object>> resultPage = new Page<>();
            resultPage.setRecords(records);
            resultPage.setTotal(listingPage.getTotal());
            resultPage.setSize(listingPage.getSize());
            resultPage.setCurrent(listingPage.getCurrent());
            resultPage.setPages(listingPage.getPages());

            log.info("分页搜索商品完成，总记录数: {}", resultPage.getTotal());
            return resultPage;
        } catch (Exception e) {
            log.error("分页搜索商品异常: {}", e.getMessage(), e);
            return new Page<>();
        }
    }
}




