package com.debuff.debuffbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.debuff.debuffbackend.entity.ItemTags;
import org.apache.ibatis.annotations.Mapper;

/**
* @author m1822
* @description 针对表【item_tags(物品标签信息表)】的数据库操作Mapper
* @createDate 2025-07-25 09:31:41
* @Entity com.debuff.debuffbackend.entity.ItemTags
*/
@Mapper
public interface ItemTagsMapper extends BaseMapper<ItemTags> {

}




