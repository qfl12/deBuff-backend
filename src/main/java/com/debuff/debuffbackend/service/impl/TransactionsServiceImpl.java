package com.debuff.debuffbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debuff.debuffbackend.entity.Transactions;
import com.debuff.debuffbackend.service.TransactionsService;
import com.debuff.debuffbackend.mapper.TransactionsMapper;
import org.springframework.stereotype.Service;

/**
* @author m1822
* @description 针对表【transactions(交易记录表)】的数据库操作Service实现
* @createDate 2025-07-09 14:55:57
*/
@Service
public class TransactionsServiceImpl extends ServiceImpl<TransactionsMapper, Transactions>
    implements TransactionsService{

}




