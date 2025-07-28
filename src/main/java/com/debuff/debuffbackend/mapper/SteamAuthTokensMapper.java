package com.debuff.debuffbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.debuff.debuffbackend.entity.SteamAuthTokens;
import org.apache.ibatis.annotations.Mapper;

/**
* @author m1822
* @description 针对表【steam_auth_tokens(Steam登录安全令牌存储表)】的数据库操作Mapper
* @createDate 2025-07-28 03:06:29
* @Entity com.debuff.debuffbackend.entity.SteamAuthTokens
*/
@Mapper
public interface SteamAuthTokensMapper extends BaseMapper<SteamAuthTokens> {

}




