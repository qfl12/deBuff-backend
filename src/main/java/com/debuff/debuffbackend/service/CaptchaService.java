package com.debuff.debuffbackend.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 验证码服务接口，定义验证码生成和验证功能
 */
public interface CaptchaService {
    /**
     * 生成验证码图片并写入响应流
     * @param response HTTP响应对象
     * @throws IOException IO异常
     */
    void generateCaptchaImage(HttpServletResponse response) throws IOException;

    /**
     * 验证用户输入的验证码是否正确
     * @param code 用户输入的验证码
     * @param session HTTP会话对象，用于获取存储的验证码
     * @return 验证结果，true为正确，false为错误
     */
    boolean verifyCaptcha(String code, HttpSession session);
}