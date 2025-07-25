package com.debuff.debuffbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debuff.debuffbackend.entity.ItemTags;
import com.debuff.debuffbackend.service.ItemTagsService;
import com.debuff.debuffbackend.mapper.ItemTagsMapper;
import org.springframework.stereotype.Service;

/**
* @author m1822
* @description 针对表【item_tags(物品标签信息表)】的数据库操作Service实现
* @createDate 2025-07-25 09:31:41
*/
@Service
public class ItemTagsServiceImpl extends ServiceImpl<ItemTagsMapper, ItemTags>
    implements ItemTagsService{

}




