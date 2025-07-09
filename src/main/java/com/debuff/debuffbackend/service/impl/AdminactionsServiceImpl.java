package com.debuff.debuffbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debuff.debuffbackend.entity.Adminactions;
import com.debuff.debuffbackend.service.AdminactionsService;
import com.debuff.debuffbackend.mapper.AdminactionsMapper;
import org.springframework.stereotype.Service;

/**
* @author m1822
* @description 针对表【adminactions(管理员操作记录表)】的数据库操作Service实现
* @createDate 2025-07-09 14:55:57
*/
@Service
public class AdminactionsServiceImpl extends ServiceImpl<AdminactionsMapper, Adminactions>
    implements AdminactionsService{

}




