package com.debuff.debuffbackend.mapper;

import com.debuff.debuffbackend.entity.Auctions;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author m1822
* @description 针对表【auctions(拍卖信息表)】的数据库操作Mapper
* @createDate 2025-07-09 14:55:57
* @Entity com.debuff.debuffbackend.entity.Auctions
*/
@Mapper
public interface AuctionsMapper extends BaseMapper<Auctions> {

}




