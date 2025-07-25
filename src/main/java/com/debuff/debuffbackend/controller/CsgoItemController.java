package com.debuff.debuffbackend.controller;

import com.debuff.debuffbackend.entity.CsgoItem;
import com.debuff.debuffbackend.mapper.CsgoItemMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CSGO物品数据API控制器
 * 提供CSGO游戏物品数据的查询接口
 */
@RestController
@RequestMapping("/api/csgo/items")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "CSGO物品接口", description = "提供CSGO游戏物品数据的查询功能")
public class CsgoItemController {

    private final CsgoItemMapper csgoItemMapper;

    /**
     * 获取所有CSGO物品
     * @return 物品列表
     */
    @GetMapping
    @Operation(summary = "获取所有CSGO物品", description = "返回系统中存储的所有CSGO物品数据")
    public ResponseEntity<List<CsgoItem>> getAllItems() {
        List<CsgoItem> items = csgoItemMapper.selectList(null);
        return ResponseEntity.ok(items);
    }

    /**
     * 根据类型获取物品
     * @param type 物品类型 (skins, stickers, crates等)
     * @return 该类型的物品列表
     */
    @GetMapping("/type/{type}")
    @Operation(summary = "按类型获取物品", description = "根据物品类型获取对应的CSGO物品数据")
    public ResponseEntity<List<CsgoItem>> getItemsByType(
            @Parameter(description = "物品类型(skins, stickers, crates等)", required = true)@PathVariable String type) {
        List<CsgoItem> items = csgoItemMapper.selectByType(type);
        return ResponseEntity.ok(items);
    }

    /**
     * 根据稀有度获取物品
     * @param rarity 稀有度
     * @return 该稀有度的物品列表
     */
    @GetMapping("/rarity/{rarity}")
    @Operation(summary = "按稀有度获取物品", description = "根据稀有度获取对应的CSGO物品数据")
    public ResponseEntity<List<CsgoItem>> getItemsByRarity(
            @Parameter(description = "物品稀有度", required = true)@PathVariable String rarity) {
        List<CsgoItem> items = csgoItemMapper.selectByRarity(rarity);
        return ResponseEntity.ok(items);
    }

    /**
     * 根据Steam物品ID获取单个物品
     * @param steamItemId Steam物品唯一标识
     * @return 单个物品详情
     */
    @GetMapping("/steam/{steamItemId}")
    @Operation(summary = "按SteamID获取物品", description = "根据Steam物品ID获取单个CSGO物品详情")
    public ResponseEntity<CsgoItem> getItemBySteamId(
            @Parameter(description = "Steam物品唯一标识(classid+instanceid)", required = true)@PathVariable String steamItemId) {
        CsgoItem item = csgoItemMapper.selectBySteamItemId(steamItemId);
        return ResponseEntity.ok(item);
    }
}