package com.debuff.debuffbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debuff.debuffbackend.entity.Auctions;
import com.debuff.debuffbackend.service.AuctionsService;
import com.debuff.debuffbackend.mapper.AuctionsMapper;
import org.springframework.stereotype.Service;

/**
* @author m1822
* @description 针对表【auctions(拍卖信息表)】的数据库操作Service实现
* @createDate 2025-07-09 14:55:57
*/
@Service
public class AuctionsServiceImpl extends ServiceImpl<AuctionsMapper, Auctions>
    implements AuctionsService{

}




