package com.debuff.debuffbackend.controller;

import com.debuff.debuffbackend.service.CaptchaService;
import com.debuff.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * 验证码控制器，处理图片验证码生成请求
 */
@RestController
@RequestMapping("/api/captcha")

public class CaptchaController {

    private final CaptchaService captchaService;

    @Autowired
    public CaptchaController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    /**
     * 生成图片验证码
     */
    @GetMapping("/image")
    public void generateCaptcha(HttpServletResponse response) throws IOException {
        // 设置响应头
        response.setContentType("image/png");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        // 生成验证码图片并写入响应流
        captchaService.generateCaptchaImage(response);
    }

    /**
     * 验证图片验证码
     */
    @PostMapping("/verify")
    public Result<Boolean> verifyCaptcha(@RequestBody Map<String, String> request, HttpSession session) {
        String code = request.get("code");
        boolean isValid = captchaService.verifyCaptcha(code, session);
        if (isValid) {
            return Result.success(true);
        } else {
            return Result.error(400, "验证码错误");
        }
    }
}