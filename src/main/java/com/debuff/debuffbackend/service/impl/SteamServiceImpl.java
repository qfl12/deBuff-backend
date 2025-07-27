package com.debuff.debuffbackend.service.impl;

import com.debuff.debuffbackend.common.Result;
import com.debuff.debuffbackend.entity.ItemTags;
import com.debuff.debuffbackend.entity.Items;
import com.debuff.debuffbackend.entity.Users;
import com.debuff.debuffbackend.model.SteamInventoryResponse;
import com.debuff.debuffbackend.service.ItemTagsService;
import com.debuff.debuffbackend.service.ItemsService;
import com.debuff.debuffbackend.service.SteamService;
import com.debuff.debuffbackend.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

import static com.baomidou.mybatisplus.core.toolkit.Wrappers.lambdaQuery;

/**
 * Steam服务实现类
 * 实现与Steam API交互的具体逻辑
 */
@Service
@Slf4j
public class SteamServiceImpl implements SteamService {

    // Steam库存API URL模板
    private static final String STEAM_INVENTORY_URL = "https://steamcommunity.com/inventory/%s/730/2?l=schinese";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ItemsService itemsService;

    @Autowired
    private ItemTagsService itemTagsService;

    @Autowired
    private UsersService usersService;

    /**
     * 获取Steam用户库存信息
     *
     * @param steamId Steam用户ID
     * @return 封装后的库存信息响应结果
     */
    @Override
    public Result<List<Items>> getSteamInventory(String steamId) {
        try {
            // 构建完整的API请求URL
            String url = String.format(STEAM_INVENTORY_URL, steamId);
            log.info("调用Steam库存API: {}", url);

            // 发送GET请求并解析响应
            SteamInventoryResponse response = restTemplate.getForObject(url, SteamInventoryResponse.class);

            // 验证API响应状态
            if (response == null) {
                log.error("Steam库存API返回空响应，用户: {}", steamId);
                return Result.fail("获取库存数据失败：API返回空响应");
            }

            // 检查Steam API返回的成功状态
            if (response.getSuccess() != 1) {
                log.error("Steam库存API调用失败，用户: {}，错误代码: {}", steamId, response.getRwgrsn());
                return Result.fail(String.format("获取库存失败：Steam返回错误代码 %d", response.getRwgrsn()));
            }

            log.info("Steam库存API调用成功，用户: {}，库存总量: {}", steamId, response.getTotalInventoryCount());

            // 处理响应数据

            List<Items> savedItems = null;
            Integer userId = null;
            // 处理资产信息并建立映射关系
            Map<String, String> assetMap = new HashMap<>();
            if (response.getAssets() != null) {
                for (SteamInventoryResponse.Asset asset : response.getAssets()) {
                    String key = asset.getAppid() + "_" + asset.getClassid() + "_" + asset.getInstanceid();
                    assetMap.put(key, asset.getAssetid());
                    log.debug("Added asset to map: {} -> {}", key, asset.getAssetid());
                }
                log.info("处理资产信息完成，共 {} 条资产记录", response.getAssets().size());
            } else {
                log.warn("Steam API响应中未包含资产信息");
            }

            if (response.getDescriptions() != null) {
                // 获取当前用户ID
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String username = authentication.getName();
                Users currentUser = usersService.getOne(lambdaQuery(Users.class).eq(Users::getUserId, username));
                if (currentUser == null) {
                    log.error("未找到用户名对应的用户: {}", username);
                    log.error("当前用户不存在");
                    return Result.fail("用户未登录");
                }
                userId = currentUser.getUserId();
                savedItems = new ArrayList<>();

                // 查询本地库存并建立映射关系
                List<Items> localItems = itemsService.list(lambdaQuery(Items.class).eq(Items::getUserId, userId));
                Map<String, Items> localItemMap = new HashMap<>();
                for (Items item : localItems) {
                    String key = item.getAppid() + "_" + item.getClassid() + "_" + item.getInstanceid();
                    localItemMap.put(key, item);
                }
                log.info("查询到用户本地库存 {} 件物品", localItems.size());

                for (SteamInventoryResponse.Description desc : response.getDescriptions()) {
                    String key = desc.getAppid() + "_" + desc.getClassid() + "_" + desc.getInstanceid();
                    if (localItemMap.containsKey(key)) {
                        // 本地已存在相同物品，进行字段比较
                        Items existingItem = localItemMap.get(key);
                        boolean needUpdate = false;

                        // 比较并更新基础信息字段（排除quantity、acquisition_time、status）
                        try {
                            // 比较appid
                            int newAppid = Integer.parseInt(desc.getAppid());
                            if (!Objects.equals(existingItem.getAppid(), newAppid)) {
                                existingItem.setAppid(newAppid);
                                needUpdate = true;
                            }

                            // 比较classid
                            long newClassid = Long.parseLong(desc.getClassid());
                            if (!Objects.equals(existingItem.getClassid(), newClassid)) {
                                existingItem.setClassid(newClassid);
                                needUpdate = true;
                            }

                            // 比较instanceid
                            long newInstanceid = Long.parseLong(desc.getInstanceid());
                            if (!Objects.equals(existingItem.getInstanceid(), newInstanceid)) {
                                existingItem.setInstanceid(newInstanceid);
                                needUpdate = true;
                            }

                            // 比较iconUrl
                            if (!Objects.equals(existingItem.getIconUrl(), desc.getIconUrl())) {
                                existingItem.setIconUrl(desc.getIconUrl());
                                needUpdate = true;
                            }

                            // 比较name
                            if (!Objects.equals(existingItem.getName(), desc.getName())) {
                                existingItem.setName(desc.getName());
                                needUpdate = true;
                            }

                            // 比较type
                            if (!Objects.equals(existingItem.getType(), desc.getType())) {
                                existingItem.setType(desc.getType());
                                needUpdate = true;
                            }

                            // 比较marketName
                            if (!Objects.equals(existingItem.getMarketName(), desc.getMarketName())) {
                                existingItem.setMarketName(desc.getMarketName());
                                needUpdate = true;
                            }

                            // 比较marketHashName
                            if (!Objects.equals(existingItem.getMarketHashName(), desc.getMarketHashName())) {
                                existingItem.setMarketHashName(desc.getMarketHashName());
                                needUpdate = true;
                            }

                            // 比较tradable
                            if (!Objects.equals(existingItem.getTradable(), desc.getTradable())) {
                                existingItem.setTradable(desc.getTradable());
                                needUpdate = true;
                            }

                            // 比较commodity
                            if (!Objects.equals(existingItem.getCommodity(), desc.getCommodity())) {
                                existingItem.setCommodity(desc.getCommodity());
                                needUpdate = true;
                            }

                            // 比较marketTradableRestriction
                            if (!Objects.equals(existingItem.getMarketTradableRestriction(), desc.getMarketTradableRestriction())) {
                                existingItem.setMarketTradableRestriction(desc.getMarketTradableRestriction());
                                needUpdate = true;
                            }

                            // 比较marketMarketableRestriction
                            if (!Objects.equals(existingItem.getMarketMarketableRestriction(), desc.getMarketMarketableRestriction())) {
                                existingItem.setMarketMarketableRestriction(desc.getMarketMarketableRestriction());
                                needUpdate = true;
                            }

                            // 更新物品描述信息
                            if (desc.getDescriptions() != null) {
                                for (SteamInventoryResponse.Description.DescriptionItem descItem : desc.getDescriptions()) {
                                    // 仅处理名称为"description"的条目，排除属性和空白条目
                            if ("description".equals(descItem.getName()) && descItem.getValue() != null && 
                                !descItem.getName().startsWith("attr:") && !"blank".equals(descItem.getName()) && !"attribute".equals(descItem.getName())) {
                                        // 移除HTML标签并清理空白字符（增强版）
                                        String cleanDescription = descItem.getValue()
                                            .replaceAll("<[^>]*>", "") // 移除所有HTML标签
                                            .replaceAll("\\p{Z}+", " ") // 替换所有Unicode空白字符为单个空格
                                            .trim(); // 去除首尾空格
                                        // 处理空描述情况
                                        if (cleanDescription.isEmpty()) {
                                            cleanDescription = "";
                                        }
                                        if (!Objects.equals(existingItem.getDescription(), cleanDescription)) {
                                            existingItem.setDescription(cleanDescription);
                                            needUpdate = true;
                                        }
                                        break;
                                    }
                                }
                            }

                            // 同步更新assetid
                            String assetKey = existingItem.getAppid() + "_" + existingItem.getClassid() + "_" + existingItem.getInstanceid();
                            String assetidStr = assetMap.get(assetKey);
                            if (assetidStr != null) {
                                try {
                                    long assetid = Long.parseLong(assetidStr);
                                    if (!Objects.equals(existingItem.getAssetid(), assetid)) {
                                        existingItem.setAssetid(assetid);
                                        needUpdate = true;
                                    }
                                } catch (NumberFormatException e) {
                                    log.error("资产ID转换失败: {}", assetidStr, e);
                                }
                            } else {
                                log.warn("未找到物品对应的资产信息: {}", assetKey);
                            }
                        } catch (NumberFormatException e) {
                            log.error("解析物品ID失败: appid={}, classid={}, instanceid={}", desc.getAppid(), desc.getClassid(), desc.getInstanceid(), e);
                            localItemMap.remove(key);
                            continue;
                        }

                        // 如果有字段变更，更新物品信息
                        if (needUpdate) {
                            // 更新修改时间（如果有该字段）
                            // existingItem.setUpdateTime(LocalDateTime.now());
                            itemsService.updateById(existingItem);
                            log.info("更新本地物品信息: {}", desc.getMarketHashName());
                        }

                        // 处理标签信息更新
                        // 先删除旧标签
                        itemTagsService.remove(lambdaQuery(ItemTags.class).eq(ItemTags::getItemId, existingItem.getId()));
                        // 添加新标签
                        if (desc.getTags() != null) {
                            for (SteamInventoryResponse.Description.Tag tag : desc.getTags()) {
                                // 移除标签中可能存在的HTML内容
                                if (tag.getLocalizedTagName() != null) {
                                    tag.setLocalizedTagName(tag.getLocalizedTagName().replaceAll("<[^>]*>", ""));
                                }

                                ItemTags itemTag = new ItemTags();
                                itemTag.setItemId(existingItem.getId());
                                itemTag.setCategory(tag.getCategory());
                                itemTag.setInternalName(tag.getInternalName());
                                itemTag.setLocalizedCategoryName(tag.getLocalizedCategoryName());
                                itemTag.setLocalizedTagName(tag.getLocalizedTagName());
                                itemTagsService.save(itemTag);
                            }
                            log.info("更新物品标签信息: {}", desc.getMarketHashName());
                        }

                        // 从映射中移除已处理物品
                        localItemMap.remove(key);
                        continue;
                    }

                    // 处理标签信息
                    if (desc.getTags() != null) {
                        for (SteamInventoryResponse.Description.Tag tag : desc.getTags()) {
                            // 移除标签中可能存在的HTML内容
                            if (tag.getLocalizedTagName() != null) {
                                tag.setLocalizedTagName(tag.getLocalizedTagName().replaceAll("<[^>]*>", ""));
                            }
                        }
                    }

                    // 创建物品对象
                    Items item = new Items();
                    item.setUserId(userId);
                    try {
                        item.setAppid(Integer.parseInt(desc.getAppid()));
                        item.setClassid(Long.parseLong(desc.getClassid()));
                        item.setInstanceid(Long.parseLong(desc.getInstanceid()));
                        
                        // 从资产映射中获取并设置assetid
                        String assetKey = desc.getAppid() + "_" + desc.getClassid() + "_" + desc.getInstanceid();
                        String assetidStr = assetMap.get(assetKey);
                        if (assetidStr != null) {
                            try {
                                // 转换assetid为Long类型
                                Long assetid = (Long) Long.parseLong(assetidStr);
                                item.setAssetid((long) Math.toIntExact(assetid));
                            } catch (NumberFormatException e) {
                                log.error("资产ID转换失败: {}", assetidStr, e);
                            }
                        } else {
                            log.warn("未找到物品对应的资产信息: {}", assetKey);
                        }
                        if (assetidStr == null) {
                            log.warn("未找到物品对应的资产信息: {}", assetKey);
                        }
                    } catch (NumberFormatException e) {
                        log.error("解析物品ID失败: appid={}, classid={}, instanceid={}", desc.getAppid(), desc.getClassid(), desc.getInstanceid(), e);
                        continue;
                    }
                    item.setIconUrl(desc.getIconUrl());
                    item.setName(desc.getName());
                    item.setType(desc.getType());
                    item.setMarketName(desc.getMarketName());
                    item.setMarketHashName(desc.getMarketHashName());
                    item.setTradable(desc.getTradable());
                    item.setCommodity(desc.getCommodity());
                    item.setMarketTradableRestriction(desc.getMarketTradableRestriction());
                    item.setMarketMarketableRestriction(desc.getMarketMarketableRestriction());

// 提取物品描述信息
if (desc.getDescriptions() != null) {
    for (SteamInventoryResponse.Description.DescriptionItem descItem : desc.getDescriptions()) {
        if ("description".equals(descItem.getName()) && descItem.getValue() != null) {
            // 移除HTML标签并清理空白字符
            // 增强版HTML清理：移除所有标签并标准化空白字符
                    String cleanDescription = descItem.getValue()
                        .replaceAll("<[^>]*>", "") // 移除所有HTML标签
                        .replaceAll("\\p{Z}+", " ") // 替换所有Unicode空白字符为单个空格
                        .trim(); // 去除首尾空格
                    // 处理空描述情况
                    if (cleanDescription.isEmpty()) {
                        cleanDescription = "";
                    }
            item.setDescription(cleanDescription);
            break;
        }
    }
}
                    item.setQuantity(1);
                    item.setAcquisitionTime(LocalDateTime.now());
                    item.setStatus("AVAILABLE");

                    // 保存物品
                    itemsService.save(item);
                    savedItems.add(item);
                    log.info("添加新物品到本地库存: {}", desc.getMarketHashName());

                    // 处理物品标签
                    if (desc.getTags() != null) {
                        for (SteamInventoryResponse.Description.Tag tag : desc.getTags()) {
                            ItemTags itemTag = new ItemTags();
                            itemTag.setItemId(item.getId());
                            itemTag.setCategory(tag.getCategory());
                            itemTag.setInternalName(tag.getInternalName());
                            itemTag.setLocalizedCategoryName(tag.getLocalizedCategoryName());
                            itemTag.setLocalizedTagName(tag.getLocalizedTagName());
                            itemTagsService.save(itemTag);
                        }
                    }


                }

                // 处理本地多余物品（Steam中不存在的物品）
                for (Items remainingItem : localItemMap.values()) {
                    // 直接删除所有本地多余物品，不考虑限制条件
                    log.info("删除本地多余物品: {}", remainingItem.getMarketHashName());
                    itemsService.removeById(remainingItem.getId());
                }
            }

            // 返回成功结果
            // 查询用户所有库存物品并返回
            List<Items> allItems = itemsService.list(lambdaQuery(Items.class).eq(Items::getUserId, userId));
            return Result.success(allItems);
        } catch (RestClientException e) {
            // 处理HTTP请求异常
            log.error("调用Steam库存API失败，用户: {}", steamId, e);
            return Result.fail("获取Steam库存失败: " + e.getMessage());
        } catch (Exception e) {
            // 处理其他未知异常
            log.error("处理Steam库存数据异常，用户: {}", steamId, e);
            return Result.fail("处理库存数据失败: " + e.getMessage());
        }
    }
}