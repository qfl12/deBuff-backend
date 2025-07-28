package com.debuff.debuffbackend.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.debuff.debuffbackend.entity.Tradeconfirmations;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author m1822
* @description 针对表【tradeconfirmations(交易确认表)】的数据库操作Mapper
* @createDate 2025-07-27 00:38:37
* @Entity com.debuff.debuffbackend.entity.Tradeconfirmations
*/
@Mapper
public interface TradeconfirmationsMapper extends BaseMapper<Tradeconfirmations> {

    List<Tradeconfirmations> selectUserTradeRecordsWithItems(
        @Param(Constants.WRAPPER) QueryWrapper<Tradeconfirmations> wrapper
    );

}




