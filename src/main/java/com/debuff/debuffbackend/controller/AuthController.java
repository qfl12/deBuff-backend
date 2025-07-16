package com.debuff.debuffbackend.controller;

import com.debuff.debuffbackend.entity.Users;
import com.debuff.debuffbackend.service.UsersService;
import com.debuff.debuffbackend.service.VerificationCodeService;
import com.debuff.debuffbackend.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.Random;

/**
 * 认证控制器，处理注册、登录和验证码相关请求
 */
@RestController
@RequestMapping("/")
public class AuthController {

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 发送注册验证码
     */
    @PostMapping("api/send-verification-code")
    public ResponseEntity<Map<String, Object>> sendRegisterCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Map<String, Object> response = new HashMap<>();

        if (email == null || email.isEmpty()) {
            response.put("success", false);
            response.put("message", "邮箱不能为空");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        boolean sent = verificationCodeService.sendCode(email);
        if (sent) {
            response.put("success", true);
            response.put("message", "验证码已发送到邮箱");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("success", false);
            response.put("message", "验证码发送失败，请重试");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 检查邮箱是否已注册
     */
    @GetMapping("api/check-email")
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();

        if (email == null || email.isEmpty()) {
            response.put("success", false);
            response.put("message", "邮箱不能为空");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Users existingUser = usersService.getOne(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Users>().eq("email", email));
        if (existingUser != null) {
            response.put("success", true);
            response.put("available", false);
            response.put("message", "该邮箱已被注册");
        } else {
            response.put("success", true);
            response.put("available", true);
            response.put("message", "邮箱可用");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 用户注册
     */
    @PostMapping("api/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String email = request.get("email");
        String password = request.get("password");
        String code = request.get("emailCode");
        Map<String, Object> response = new HashMap<>();

        // 参数校验
        if (username == null || username.isEmpty() || email == null || email.isEmpty() ||
                password == null || password.isEmpty() || code == null || code.isEmpty()) {
            response.put("success", false);
            response.put("message", "所有字段均为必填项");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // 验证验证码
        boolean codeValid = verificationCodeService.validateCode(email, code);
        if (!codeValid) {
            response.put("success", false);
            response.put("message", "验证码无效或已过期");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // 检查邮箱是否已注册
        Users existingUser = usersService.getOne(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Users>().eq("email", email));
        if (existingUser != null) {
            response.put("success", false);
            response.put("message", "该邮箱已被注册");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // 创建新用户
        Users user = new Users();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRegisterTime(new Date());
        user.setStatus("active"); // 设置用户状态为激活
        user.setRole("user"); // 默认角色为普通用户
        user.setIsVerified(0); // 未实名认证

        // 随机选择默认头像
        Random random = new Random();
        int avatarNumber = random.nextInt(5) + 1; // 生成1-5的随机数
        String avatarUrl = "/assets/avatars/default-avatar-" + avatarNumber + ".svg";
        user.setAvatarUrl(avatarUrl);

        boolean saved = usersService.save(user);
        if (saved) {
            response.put("success", true);
            response.put("message", "注册成功");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            response.put("success", false);
            response.put("message", "注册失败，请重试");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 发送登录验证码
     */
    @PostMapping("api/login/send-verification-code")
    public ResponseEntity<Map<String, Object>> sendLoginCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Map<String, Object> response = new HashMap<>();

        if (email == null || email.isEmpty()) {
            response.put("success", false);
            response.put("message", "邮箱不能为空");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // 检查邮箱是否已注册
        Users user = usersService.findByEmail(email);
        if (user == null) {
            response.put("success", false);
            response.put("message", "该邮箱未注册");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        boolean sent = verificationCodeService.sendCode(email);
        if (sent) {
            response.put("success", true);
            response.put("message", "验证码已发送到邮箱");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("success", false);
            response.put("message", "验证码发送失败，请重试");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("api/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        String code = request.get("code");
        Map<String, Object> response = new HashMap<>();

        // 参数校验
        if (email == null || email.isEmpty() || password == null || password.isEmpty() ||
                code == null || code.isEmpty()) {
            response.put("success", false);
            response.put("message", "所有字段均为必填项");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // 验证验证码
        boolean codeValid = verificationCodeService.validateCode(email, code);
        if (!codeValid) {
            response.put("success", false);
            response.put("message", "验证码无效或已过期");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // 用户登录
        Users user = usersService.login(email, password);
        if (user == null) {
            response.put("success", false);
            response.put("message", "邮箱或密码错误");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        // 生成JWT令牌
        String token = jwtUtils.generateToken(user.getUserId());

        response.put("success", true);
        response.put("message", "登录成功");
        response.put("token", token);
        response.put("user", user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}