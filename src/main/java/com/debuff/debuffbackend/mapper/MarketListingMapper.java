package com.debuff.debuffbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.debuff.debuffbackend.entity.MarketListing;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.MapKey;

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

}




