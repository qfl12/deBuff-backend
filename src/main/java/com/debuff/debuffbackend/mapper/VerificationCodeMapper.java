package com.debuff.debuffbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.debuff.debuffbackend.entity.VerificationCode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author m1822
 * @description 针对表【verification_code(验证码表)】的数据库操作Mapper
 */
@Mapper
public interface VerificationCodeMapper extends BaseMapper<VerificationCode> {
}