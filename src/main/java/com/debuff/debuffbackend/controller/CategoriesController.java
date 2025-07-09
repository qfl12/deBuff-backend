package com.debuff.debuffbackend.controller;

import com.debuff.debuffbackend.entity.Categories;
import com.debuff.debuffbackend.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商品分类表(Categories)表控制层
 *
 * @author m1822
 * @since 2025-07-09 20:58:45
 */
@RestController
@RequestMapping("categories")
public class CategoriesController {

    /**
     * 服务对象
     */
    @Autowired
    private CategoriesService categoriesService;

}
