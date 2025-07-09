package com.debuff.debuffbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debuff.debuffbackend.entity.Orders;
import com.debuff.debuffbackend.service.OrdersService;
import com.debuff.debuffbackend.mapper.OrdersMapper;
import org.springframework.stereotype.Service;

/**
* @author m1822
* @description 针对表【orders(订单信息表)】的数据库操作Service实现
* @createDate 2025-07-09 14:55:57
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

}




