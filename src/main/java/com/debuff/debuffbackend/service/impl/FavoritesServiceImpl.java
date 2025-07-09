package com.debuff.debuffbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debuff.debuffbackend.entity.Favorites;
import com.debuff.debuffbackend.service.FavoritesService;
import com.debuff.debuffbackend.mapper.FavoritesMapper;
import org.springframework.stereotype.Service;

/**
* @author m1822
* @description 针对表【favorites(用户收藏信息表)】的数据库操作Service实现
* @createDate 2025-07-09 14:55:57
*/
@Service
public class FavoritesServiceImpl extends ServiceImpl<FavoritesMapper, Favorites>
    implements FavoritesService{

}




