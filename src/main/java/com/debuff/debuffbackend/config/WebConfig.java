package com.debuff.debuffbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Web配置类
 * 配置静态资源访问、拦截器等Web相关设置
 * @author m1822
 * @since 2025-07-10
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    @Value("${avatar.storage.path:d:/Project/Java/Debuff/uploads/avatars}")
    private String avatarStoragePath;

    /**
     * 配置外部头像存储目录的资源访问
     * 将URL路径/avatars/**映射到实际的头像存储目录
     * @param registry 资源处理器注册表
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        logger.info("Configuring avatar resource handler: mapping /avatars/** to {}", avatarStoragePath);
        // 配置头像资源访问路径，使用file协议访问本地文件系统
        registry.addResourceHandler("src/main/resources/static/avatars/**")
                .addResourceLocations("file:" + avatarStoragePath + "/");
    }
}