package com.debuff.debuffbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.debuff.debuffbackend.entity.VerificationCode;

/**
 * @author m1822
 * @description 针对表【verification_code(验证码表)】的数据库操作Service
 */
public interface VerificationCodeService extends IService<VerificationCode> {
    /**
     * 发送验证码到邮箱
     * @param email 目标邮箱
     * @return 是否发送成功
     */
    boolean sendCode(String email);

    /**
     * 验证验证码是否有效
     * @param email 邮箱
     * @param code 验证码
     * @return 是否有效
     */
    boolean validateCode(String email, String code);
}