package com.debuff.debuffbackend.controller;

import com.debuff.debuffbackend.common.Result;
import com.debuff.debuffbackend.entity.Users;
import com.debuff.debuffbackend.service.CaptchaService;
import com.debuff.debuffbackend.service.UsersService;
import com.debuff.debuffbackend.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户信息表(Users)表控制层
 *
 * @author m1822
 * @since 2025-07-09 20:58:46
 */
@RestController
@RequestMapping("api/users")
public class UsersController {

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    // 注入头像存储路径配置
    @Value("${avatar.storage.path:d:/Project/Java/Debuff/uploads/avatars}")
    private String avatarStoragePath;
    /**
     * 服务对象
     */
    @Autowired
    private UsersService usersService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 发送密码重置邮箱验证码
     */
    @PostMapping("/send-reset-code")
    public ResponseEntity<Map<String, Object>> sendResetCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        logger.info("发送密码重置验证码 - 邮箱: {}", email);
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

        // 生成并发送验证码
        try {
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
        } catch (Exception e) {
            logger.error("发送验证码失败", e);
            response.put("success", false);
            response.put("message", "验证码发送失败，请重试");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 验证邮箱验证码并重置密码
     */
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        String newPassword = request.get("newPassword");
        logger.info("重置密码 - 邮箱: {}", email);
        Map<String, Object> response = new HashMap<>();

        // 参数验证
        if (email == null || email.isEmpty() || code == null || code.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            response.put("success", false);
            response.put("message", "邮箱、验证码和新密码均为必填项");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // 验证验证码
        boolean codeValid = verificationCodeService.validateCode(email, code);
        if (!codeValid) {
            response.put("success", false);
            response.put("message", "验证码错误或已过期");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // 更新密码
        Users user = usersService.findByEmail(email);
        if (user == null) {
            response.put("success", false);
            response.put("message", "该邮箱未注册");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        boolean updateSuccess = usersService.updateById(user);
        if (updateSuccess) {
            SecurityContextHolder.clearContext();
            response.put("success", true);
            response.put("message", "密码重置成功，请重新登录");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("success", false);
            response.put("message", "密码重置失败");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/info")
    public Result getCurrentUserInfo() {
        logger.info("Received request to get current user info");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || "anonymousUser".equals(auth.getName())) {
            logger.warn("Unauthorized access attempt to user info");
            return Result.fail("请先登录");
        }

        Integer userId;
        try {
            userId = Integer.parseInt(auth.getName());
            logger.debug("Parsed user ID: {}", userId);
        } catch (NumberFormatException e) {
            logger.error("Invalid user ID format: {}", auth.getName(), e);
            return Result.fail("非法的用户身份");
        }

        Users user = usersService.getById(userId);
        if (user == null) {
            logger.warn("User not found with ID: {}", userId);
            return Result.fail("用户不存在");
        }

        user.setPassword(null);
        logger.info("Successfully retrieved user info for ID: {}", userId);
        return Result.success(user);
    }

    /**
     * 更新用户基本信息
     * 处理用户昵称、性别、出生日期和个人简介的更新
     */
    @PutMapping("/update")
    public Result updateUserInfo(@RequestBody Users userInfo) {
        logger.info("Received request to update user info");
        // 获取当前登录用户ID
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        Integer userId = Integer.parseInt(userIdStr);
        logger.debug("Updating user info for ID: {}", userId);

        Users user = usersService.getById(userId);
        if (user == null) {
            logger.warn("User not found with ID: {}", userId);
            return Result.fail("用户不存在");
        }

        // 更新允许修改的字段
        if (userInfo.getUsername() != null) {
            user.setUsername(userInfo.getUsername());
        }
        if (userInfo.getGender() != null) {
            user.setGender(userInfo.getGender());
        }
        if (userInfo.getBirthDate() != null) {
            user.setBirthDate(userInfo.getBirthDate());
        }
        if (userInfo.getBio() != null) {
            user.setBio(userInfo.getBio());
        }
        // 添加API Key和交易链接的更新
        if (userInfo.getApiKey() != null) {
            logger.debug("Updating API Key for user: {}", userId);
            user.setApiKey(userInfo.getApiKey());
        }
        if (userInfo.getTradeLink() != null) {
            logger.debug("Updating trade link for user: {}", userId);
            user.setTradeLink(userInfo.getTradeLink());
        }

        boolean updateSuccess = usersService.updateById(user);
        if (updateSuccess) {
            logger.info("Successfully updated user info for ID: {}", userId);
            return Result.success(user);
        } else {
            logger.error("Failed to update user info for ID: {}", userId);
            return Result.fail("更新用户信息失败");
        }
    }

    /**
     * 上传用户头像
     * 处理用户头像的上传、验证、存储和更新操作
     * @param file 上传的头像文件
     * @return 上传结果，成功时包含头像URL
     */
    @PostMapping("/avatar")
    public Result uploadAvatar(@RequestParam("file") MultipartFile file) {
        logger.info("Received avatar upload request");
        // 获取当前登录用户ID
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        Integer userId = Integer.parseInt(userIdStr);
        logger.debug("Processing avatar upload for user ID: {}", userId);

        File userDir = null;
        // 使用配置的存储路径替代classpath路径
        File avatarDir = new File(avatarStoragePath);
        // 如果资源目录不存在则创建
        if (!avatarDir.exists()) {
            boolean avatarDirCreated = avatarDir.mkdirs();
            if (!avatarDirCreated) {
                logger.error("Failed to create main avatar directory at {}", avatarStoragePath);
                return Result.fail("创建头像存储目录失败");
            }
        }
        // 创建用户专属目录
        userDir = new File(avatarDir, userId.toString());
        if (!userDir.exists()) {
            boolean mkdirsSuccess = userDir.mkdirs();
            if (!mkdirsSuccess) {
                logger.error("Failed to create avatar directory for user: {}", userId);
                return Result.fail("创建用户头像目录失败");
            } else {
                logger.debug("Created/verified avatar directory for user: {}", userId);
            }
        }

        // 验证文件是否为空
        if (file.isEmpty()) {
            logger.warn("User {} attempted to upload empty avatar file", userId);
            return Result.fail("上传文件不能为空");
        }

        // 验证文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            logger.warn("User {} uploaded invalid file type: {}", userId, contentType);
            return Result.fail("只允许上传图片文件");
        }

        // 验证文件大小（2MB）
        long maxSize = 2 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            logger.warn("User {} uploaded file exceeding size limit: {}MB", userId, file.getSize()/(1024*1024));
            return Result.fail("文件大小不能超过2MB");
        }

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.lastIndexOf(".") == -1) {
            logger.warn("User {} uploaded file with invalid name: {}", userId, originalFilename);
            return Result.fail("无效的文件名");
        }
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID() + suffix;
        File destFile = new File(userDir, fileName);

        try {
            // 保存文件
            file.transferTo(destFile);

            // 更新用户头像URL
            Users user = usersService.getById(userId);
            String avatarUrl = "/avatars/" + userId + "/" + fileName;
            user.setAvatarUrl(avatarUrl);
            usersService.updateById(user);

            logger.info("Updated avatar URL for user {}: {}", userId, avatarUrl);
            return Result.success("头像上传成功");
        } catch (IOException e) {
            logger.error("Avatar upload failed for user {}: {}", userId, e.getMessage(), e);
            return Result.fail("头像上传失败：" + e.getMessage());
        }
    }

}
