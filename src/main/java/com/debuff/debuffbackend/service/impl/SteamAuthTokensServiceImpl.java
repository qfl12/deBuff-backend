package com.debuff.debuffbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debuff.debuffbackend.entity.SteamAuthTokens;
import com.debuff.debuffbackend.service.SteamAuthTokensService;
import com.debuff.debuffbackend.mapper.SteamAuthTokensMapper;
import org.springframework.stereotype.Service;

/**
* @author m1822
* @description 针对表【steam_auth_tokens(Steam登录安全令牌存储表)】的数据库操作Service实现
* @createDate 2025-07-28 03:06:29
*/
@Service
public class SteamAuthTokensServiceImpl extends ServiceImpl<SteamAuthTokensMapper, SteamAuthTokens>
    implements SteamAuthTokensService{

}




