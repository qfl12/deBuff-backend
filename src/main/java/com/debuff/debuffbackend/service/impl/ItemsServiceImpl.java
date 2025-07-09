package com.debuff.debuffbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debuff.debuffbackend.entity.Items;
import com.debuff.debuffbackend.service.ItemsService;
import com.debuff.debuffbackend.mapper.ItemsMapper;
import org.springframework.stereotype.Service;

/**
* @author m1822
* @description 针对表【items(商品信息表)】的数据库操作Service实现
* @createDate 2025-07-09 14:55:57
*/
@Service
public class ItemsServiceImpl extends ServiceImpl<ItemsMapper, Items>
    implements ItemsService{

}




