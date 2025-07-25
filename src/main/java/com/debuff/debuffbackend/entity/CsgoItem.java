package com.debuff.debuffbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * CSGO物品实体类，用于存储从CSGO-API获取的游戏物品基础数据
 */
@Data
@Entity
@Table(name = "csgo_items")
public class CsgoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 物品唯一标识符 (Steam API中的classid+instanceid组合)
     */
    @Column(unique = true, nullable = false, length = 50)
    private String steamItemId;

    /**
     * 物品名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 物品类型 (皮肤、贴纸、箱子等)
     */
    @Column(nullable = false)
    private String type;

    /**
     * 物品子类型 (武器、手套、音乐盒等)
     */
    private String subType;

    /**
     * 稀有度
     */
    private String rarity;

    /**
     * 物品描述
     */
    @Column(length = 500)
    private String description;

    /**
     * 图片URL
     */
    @Column(length = 255)
    private String imageUrl;

    /**
     * 本地图片路径
     */
    @Column(length = 255)
    private String localImagePath;

    /**
     * 收藏品名称
     */
    private String collection;

    /**
     * 是否为 stattrak 物品
     */
    private Boolean stattrak;

    /**
     * 是否为纪念品
     */
    private Boolean souvenir;

    /**
     * 创建时间
     */
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}