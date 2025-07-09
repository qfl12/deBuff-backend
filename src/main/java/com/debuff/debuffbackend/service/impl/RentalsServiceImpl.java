package com.debuff.debuffbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debuff.debuffbackend.entity.Rentals;
import com.debuff.debuffbackend.service.RentalsService;
import com.debuff.debuffbackend.mapper.RentalsMapper;
import org.springframework.stereotype.Service;

/**
* @author m1822
* @description 针对表【rentals(租赁信息表)】的数据库操作Service实现
* @createDate 2025-07-09 14:55:57
*/
@Service
public class RentalsServiceImpl extends ServiceImpl<RentalsMapper, Rentals>
    implements RentalsService{

}




