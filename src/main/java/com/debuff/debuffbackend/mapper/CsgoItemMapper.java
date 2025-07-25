package com.debuff.debuffbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.debuff.debuffbackend.entity.CsgoItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * CSGO物品数据访问接口
 * 提供对csgo_items表的CRUD操作及自定义查询方法
 */
@Mapper
public interface CsgoItemMapper extends BaseMapper<CsgoItem> {
    /**
     * 根据Steam物品ID查询物品
     * @param steamItemId Steam物品唯一标识 (classid+instanceid)
     * @return CsgoItem对象
     */
    CsgoItem selectBySteamItemId(@Param("steamItemId") String steamItemId);

    /**
     * 批量插入或更新CSGO物品
     * @param items 物品列表
     * @return 影响行数
     */
    int batchInsertOrUpdate(@Param("list") List<CsgoItem> items);

    /**
     * 根据物品类型查询
     * @param type 物品类型
     * @return 物品列表
     */
    List<CsgoItem> selectByType(@Param("type") String type);

    /**
     * 根据稀有度查询
     * @param rarity 稀有度
     * @return 物品列表
     */
    List<CsgoItem> selectByRarity(@Param("rarity") String rarity);
}