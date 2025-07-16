package com.debuff.debuffbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debuff.debuffbackend.entity.VerificationCode;
import com.debuff.debuffbackend.mapper.VerificationCodeMapper;
import com.debuff.debuffbackend.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Random;

/**
 * @author m1822
 * @description 针对表【verification_code(验证码表)】的数据库操作Service实现
 */
@Service
public class VerificationCodeServiceImpl extends ServiceImpl<VerificationCodeMapper, VerificationCode> implements VerificationCodeService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VerificationCodeMapper verificationCodeMapper;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${verification.code.expire-time}")
    private int expireTime;

    @Value("${verification.code.length}")
    private int codeLength;

    @Override
    public boolean sendCode(String email) {
        // 生成随机验证码
        String code = generateCode(codeLength);

        // 创建验证码记录
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setCode(code);
        verificationCode.setCreateTime(new Date());
        verificationCode.setExpireTime(new Date(System.currentTimeMillis() + expireTime * 60 * 1000));
        verificationCode.setStatus(0); // 0-未使用

        // 保存到数据库
        int insert = verificationCodeMapper.insert(verificationCode);
        if (insert <= 0) {
            return false;
        }

        // 发送邮件
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("【Debuff】您的验证码");
            message.setText("您的验证码是：" + code + "，有效期" + expireTime + "分钟，请尽快使用。");
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            log.error("发送验证码邮件失败：", e);
            // 发送失败时删除验证码记录
            verificationCodeMapper.deleteById(verificationCode.getId());
            return false;
        }
    }

    @Override
    public boolean validateCode(String email, String code) {
        // 查询未使用且未过期的验证码
        QueryWrapper<VerificationCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email)
                .eq("code", code)
                .eq("status", 0)
                .gt("expire_time", new Date());

        VerificationCode verificationCode = verificationCodeMapper.selectOne(queryWrapper);
        if (verificationCode == null) {
            return false;
        }

        // 标记为已使用
        verificationCode.setStatus(1);
        verificationCodeMapper.updateById(verificationCode);
        return true;
    }

    /**
     * 生成随机验证码
     */
    private String generateCode(int length) {
        String digits = "0123456789";
        Random random = new Random();
        StringBuilder codeBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            codeBuilder.append(digits.charAt(random.nextInt(digits.length())));
        }
        return codeBuilder.toString();
    }
}