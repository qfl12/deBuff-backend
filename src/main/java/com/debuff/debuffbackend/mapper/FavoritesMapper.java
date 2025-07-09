package com.debuff.debuffbackend.mapper;

import com.debuff.debuffbackend.entity.Favorites;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author m1822
* @description 针对表【favorites(用户收藏信息表)】的数据库操作Mapper
* @createDate 2025-07-09 14:55:57
* @Entity com.debuff.debuffbackend.entity.Favorites
*/
@Mapper
public interface FavoritesMapper extends BaseMapper<Favorites> {

}




