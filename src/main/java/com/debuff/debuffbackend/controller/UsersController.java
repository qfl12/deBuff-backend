package com.debuff.debuffbackend.controller;

import com.debuff.debuffbackend.common.Result;
import com.debuff.debuffbackend.entity.Users;
import com.debuff.debuffbackend.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
