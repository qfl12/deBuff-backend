package com.debuff.debuffbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.debuff.debuffbackend.entity.Items;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ItemsMapper extends BaseMapper<Items> {
    /**
     * 根据用户ID查询库存物品
     * @param userId 用户ID
     * @return 物品列表
     */
    @Select("SELECT * FROM items WHERE user_id = #{userId}")
    List<Items> selectByUserId(Integer userId);

}




