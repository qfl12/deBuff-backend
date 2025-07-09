package com.debuff.debuffbackend.controller;

import com.debuff.debuffbackend.entity.Favorites;
import com.debuff.debuffbackend.service.FavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户收藏信息表(Favorites)表控制层
 *
 * @author m1822
 * @since 2025-07-09 20:58:45
 */
@RestController
@RequestMapping("favorites")
public class FavoritesController {

    /**
     * 服务对象
     */
    @Autowired
    private FavoritesService favoritesService;

}
