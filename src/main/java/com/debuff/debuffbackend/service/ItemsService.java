package com.debuff.debuffbackend.service;

import com.debuff.debuffbackend.entity.Items;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author m1822
* @description 针对表【items(物品信息表)】的数据库操作Service
* @createDate 2025-07-25 09:31:41
*/
import java.util.List;

public interface ItemsService extends IService<Items> {
    /**
     * 根据用户ID查询库存物品
     * @param userId 用户ID
     * @return 物品列表
     */
    List<Items> getItemsByUserId(Integer userId);

}
