package com.debuff.debuffbackend.service.impl;

import com.debuff.debuffbackend.service.CaptchaService;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 验证码服务实现类
 * <p>使用 EasyCaptcha 生成图形验证码，并把验证码内容暂存到用户会话(Session)中</p>
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {
    private static final Logger logger = LoggerFactory.getLogger(CaptchaServiceImpl.class);

    /**
     * 生成验证码图片并输出到 HTTP 响应流
     *
     * @param response HttpServletResponse 响应对象，用于将图片直接写给前端
     * @throws IOException 输出流异常
     */
    @Override
    public void generateCaptchaImage(HttpServletResponse response) throws IOException {
        // 获取当前请求的Session
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        // 设置响应头信息
        response.setContentType("image/png");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        
        // 1. 创建验证码对象：宽 130px，高 48px，4 位字符
        SpecCaptcha captcha = new SpecCaptcha(130, 48, 4);
        // 2. 设置字符类型：数字+字母混合
        captcha.setCharType(Captcha.TYPE_DEFAULT);
        // 3. 获取验证码文本并统一转小写
        String code = captcha.text().toLowerCase();
        System.out.println("生成验证码: " + code); // 调试日志
        // 4. 使用传入的session存储验证码
        session.setAttribute("captchaCode", code);
        session.setMaxInactiveInterval(3 * 60);
        // 5. 将验证码图片输出到响应流
        try {
            captcha.out(response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception e) {
            System.err.println("验证码图片生成失败: " + e.getMessage());
            logger.error("验证码图片生成失败", e);
            throw new IOException("验证码图片生成失败", e);
        }
    }

    /**
     * 校验用户输入的验证码是否正确
     *
     * @param code 用户提交的验证码字符串
     * @param session HTTP会话对象，用于获取存储的验证码
     * @return true  校验通过；false 校验失败或验证码不存在
     */
    @Override
    public boolean verifyCaptcha(String code, HttpSession session) {
        // 1. 空值快速失败
        if (code == null || code.isEmpty()) {
            return false;
        }
        // 2. 取出服务器端保存的验证码文本
        String storedCode = (String) session.getAttribute("captchaCode");
        if (storedCode == null) {
            return false;
        }
        // 3. 忽略大小写比较
        boolean isValid = storedCode.equalsIgnoreCase(code);
        // 4. 验证成功后立即移除验证码，防止重复使用
        if (isValid) {
            session.removeAttribute("captchaCode");
        }
        return isValid;
    }
}