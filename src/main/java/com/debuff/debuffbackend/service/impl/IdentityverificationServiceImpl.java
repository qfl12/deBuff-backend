package com.debuff.debuffbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debuff.debuffbackend.entity.Identityverification;
import com.debuff.debuffbackend.service.IdentityverificationService;
import com.debuff.debuffbackend.mapper.IdentityverificationMapper;
import org.springframework.stereotype.Service;

/**
* @author m1822
* @description 针对表【identityverification(实名认证信息表)】的数据库操作Service实现
* @createDate 2025-07-09 14:55:57
*/
@Service
public class IdentityverificationServiceImpl extends ServiceImpl<IdentityverificationMapper, Identityverification>
    implements IdentityverificationService{

}




