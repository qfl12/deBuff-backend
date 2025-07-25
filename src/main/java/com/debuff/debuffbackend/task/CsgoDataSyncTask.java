package com.debuff.debuffbackend.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.debuff.debuffbackend.entity.CsgoItem;
import com.debuff.debuffbackend.mapper.CsgoItemMapper;
import com.debuff.debuffbackend.service.CsgoApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * CSGO数据同步定时任务
 * 定期从本地CSGO-API服务拉取最新物品数据并更新到数据库
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CsgoDataSyncTask {

    private final CsgoApiService csgoApiService;
    private final CsgoItemMapper csgoItemMapper;

    /**
     * 每日凌晨2点执行CSGO物品数据全量同步
     * 同步顺序：皮肤 -> 贴纸 -> 箱子 -> 其他类型
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void syncAllCsgoItems() {
        log.info("开始执行CSGO物品数据全量同步任务");
        long startTime = System.currentTimeMillis();

        try {
            // 1. 同步皮肤数据
            int skinsCount = syncItemsByType("skins");
            log.info("皮肤数据同步完成，处理 {} 条记录", skinsCount);

            // 2. 同步贴纸数据
            int stickersCount = syncItemsByType("stickers");
            log.info("贴纸数据同步完成，处理 {} 条记录", stickersCount);

            // 3. 同步箱子数据
            int cratesCount = syncItemsByType("crates");
            log.info("箱子数据同步完成，处理 {} 条记录", cratesCount);

            // 4. 同步其他类型数据（可根据需要扩展）
            // int agentsCount = syncItemsByType("agents");
            // log.info("角色数据同步完成，处理 {} 条记录", agentsCount);

            long endTime = System.currentTimeMillis();
            log.info("CSGO物品数据全量同步任务完成，总耗时: {} 毫秒", endTime - startTime);
        } catch (Exception e) {
            log.error("CSGO物品数据同步任务失败", e);
        }
    }

    /**
     * 根据物品类型同步数据
     * @param type 物品类型（skins, stickers, crates等）
     * @return 处理的记录数
     * @throws IOException 当API请求失败时抛出
     */
    private int syncItemsByType(String type) throws IOException {
        // 1. 从CSGO-API获取数据（默认使用中文）
        String jsonData = csgoApiService.getItemsDataByType(type, "zh-CN");
        if (jsonData == null || jsonData.isEmpty()) {
            log.warn("未获取到 {} 类型的CSGO物品数据", type);
            return 0;
        }

        // 2. 解析JSON数据为CsgoItem列表
        List<CsgoItem> items = parseItemsFromJson(type, jsonData);
        if (items.isEmpty()) {
            log.warn("解析后未得到 {} 类型的CSGO物品数据", type);
            return 0;
        }

        // 3. 批量插入或更新数据库
        int affectedRows = csgoItemMapper.batchInsertOrUpdate(items);
        return affectedRows;
    }

    /**
     * 将JSON数据解析为CsgoItem列表
     * @param type 物品类型
     * @param jsonData JSON字符串
     * @return CsgoItem列表
     */
    private List<CsgoItem> parseItemsFromJson(String type, String jsonData) {
        List<CsgoItem> items = new ArrayList<>();
        JSONArray jsonArray = JSONArray.parseArray(jsonData);

        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;
            CsgoItem item = new CsgoItem();

            // 设置基础属性
            item.setSteamItemId(buildSteamItemId(jsonObject));
            item.setName(jsonObject.getString("name"));
            item.setType(type);
            item.setSubType(jsonObject.getString("subType"));
            item.setRarity(jsonObject.getString("rarity"));
            item.setDescription(jsonObject.getString("description"));
            item.setImageUrl(jsonObject.getString("image"));
            item.setCollection(jsonObject.getString("collection"));

            // 设置特殊属性（根据不同类型物品扩展）
            if ("skins".equals(type)) {
                item.setStattrak(jsonObject.getBoolean("stattrak"));
                item.setSouvenir(jsonObject.getBoolean("souvenir"));
            }

            items.add(item);
        }

        return items;
    }

    /**
     * 构建Steam物品ID (classid+instanceid)
     * @param jsonObject 物品JSON对象
     * @return 组合后的Steam物品ID
     */
    private String buildSteamItemId(JSONObject jsonObject) {
        String classId = jsonObject.getString("classid");
        String instanceId = jsonObject.getString("instanceid");
        return classId + "_" + instanceId;
    }
}