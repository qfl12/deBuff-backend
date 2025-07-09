package com.debuff.debuffbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debuff.debuffbackend.entity.Categories;
import com.debuff.debuffbackend.service.CategoriesService;
import com.debuff.debuffbackend.mapper.CategoriesMapper;
import org.springframework.stereotype.Service;

/**
* @author m1822
* @description 针对表【categories(商品分类表)】的数据库操作Service实现
* @createDate 2025-07-09 14:55:57
*/
@Service
public class CategoriesServiceImpl extends ServiceImpl<CategoriesMapper, Categories>
    implements CategoriesService{

}




