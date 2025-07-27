package com.debuff.debuffbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debuff.debuffbackend.entity.Transactions;
import com.debuff.debuffbackend.mapper.TransactionsMapper;
import com.debuff.debuffbackend.service.TransactionsService;
import org.springframework.stereotype.Service;

/**
* @author m1822
* @description 针对表【transactions(交易记录表)】的数据库操作Service实现
* @createDate 2025-07-27 00:38:37
*/
@Service
public class TransactionsServiceImpl extends ServiceImpl<TransactionsMapper, Transactions>
    implements TransactionsService{

}




