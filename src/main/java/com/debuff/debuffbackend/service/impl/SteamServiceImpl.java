package com.debuff.debuffbackend.service.impl;

import com.debuff.debuffbackend.model.SteamInventoryResponse;
import com.debuff.debuffbackend.service.SteamService;
import com.debuff.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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

    /**
     * 获取Steam用户库存信息
     * @param steamId Steam用户ID
     * @return 封装后的库存信息响应结果
     */
    @Override
    public Result<SteamInventoryResponse> getSteamInventory(String steamId) {
        try {
            // 构建完整的API请求URL
            String url = String.format(STEAM_INVENTORY_URL, steamId);
            log.info("调用Steam库存API: {}", url);

            // 发送GET请求并解析响应
            SteamInventoryResponse response = restTemplate.getForObject(url, SteamInventoryResponse.class);

            // 验证API响应状态
            if (response == null) {
                log.error("Steam库存API返回空响应，用户: {}", steamId);
                return Result.error(500, "获取库存数据失败：API返回空响应");
            }

            // 检查Steam API返回的成功状态
            if (response.getSuccess() != 1) {
                log.error("Steam库存API调用失败，用户: {}，错误代码: {}", steamId, response.getRwgrsn());
                return Result.error(500, String.format("获取库存失败：Steam返回错误代码 %d", response.getRwgrsn()));
            }

            log.info("Steam库存API调用成功，用户: {}，库存总量: {}", steamId, response.getTotalInventoryCount());

            // 处理响应数据
            if (response.getDescriptions() != null) {
                // 基础URL前缀
                String baseIconUrl = "https://cdn.steamstatic.com/apps/730/icons/";
                
                for (SteamInventoryResponse.Description desc : response.getDescriptions()) {
                    // 处理图标URL
                    if (desc.getIconUrl() != null && !desc.getIconUrl().isEmpty()) {
                        desc.setIconUrl(baseIconUrl + desc.getIconUrl());
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

                    // 处理物品描述中的HTML内容
                    if (desc.getDescriptions() != null) {
                        for (SteamInventoryResponse.Description.DescriptionItem item : desc.getDescriptions()) {
                            if (item.getValue() != null) {
                                // 移除HTML标签并清理空白字符
                                String cleanValue = item.getValue().replaceAll("<[^>]*>", "").trim().replaceAll("\\s+", " ");
                                item.setValue(cleanValue);
                            }
                        }
                    }
                }
            }

            // 返回成功结果
            return Result.success(response);
        } catch (RestClientException e) {
            // 处理HTTP请求异常
            log.error("调用Steam库存API失败，用户: {}", steamId, e);
            return Result.error(500, "获取Steam库存失败: " + e.getMessage());
        } catch (Exception e) {
            // 处理其他未知异常
            log.error("处理Steam库存数据异常，用户: {}", steamId, e);
            return Result.error(500, "处理库存数据失败: " + e.getMessage());
        }
    }
}