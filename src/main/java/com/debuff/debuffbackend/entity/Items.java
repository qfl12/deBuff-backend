package com.debuff.debuffbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 物品信息表
 * @TableName items
 */
@TableName(value ="items")
@Data
public class Items {
    /**
     * 自增主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID（外键）
     */
    private Integer userId;

    /**
     * 应用ID，用于区分不同游戏或应用
     */
    private Integer appid;

    /**
     * 类别ID，标识物品类别
     */
    private Long classid;

    /**
     * 实例ID，进一步细化物品标识
     */
    private Long instanceid;

    /**
     * 物品图标URL
     */
    @JsonProperty("imageUrl")
    private String iconUrl;

    /**
     * 物品名称
     */
    private String name;

    /**
     * 物品类别
     */
    private String type;

    /**
     * 市场中显示的物品名称
     */
    private String marketName;

    /**
     * 用于市场查询的唯一标识符
     */
    private String marketHashName;

    /**
     * 物品描述信息
     */
    private String description;

    /**
     * 是否可交易：1为是，0为否
     */
    private Integer tradable;

    /**
     * 是否为商品：1为是，0为否
     */
    private Integer commodity;

    /**
     * 市场交易限制
     */
    private Integer marketTradableRestriction;

    /**
     * 市场销售限制
     */
    private Integer marketMarketableRestriction;

    /**
     * 物品数量
     */
    private Integer quantity;

    /**
     * 获取时间
     */
    private LocalDateTime acquisitionTime;

    /**
     * 物品状态：AVAILABLE-可用, ON_SALE-在售, TRADING-交易中, DELETED-已删除
     */
    private String status;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Items other = (Items) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getAppid() == null ? other.getAppid() == null : this.getAppid().equals(other.getAppid()))
            && (this.getClassid() == null ? other.getClassid() == null : this.getClassid().equals(other.getClassid()))
            && (this.getInstanceid() == null ? other.getInstanceid() == null : this.getInstanceid().equals(other.getInstanceid()))
            && (this.getIconUrl() == null ? other.getIconUrl() == null : this.getIconUrl().equals(other.getIconUrl()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getMarketName() == null ? other.getMarketName() == null : this.getMarketName().equals(other.getMarketName()))
            && (this.getMarketHashName() == null ? other.getMarketHashName() == null : this.getMarketHashName().equals(other.getMarketHashName()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getTradable() == null ? other.getTradable() == null : this.getTradable().equals(other.getTradable()))
            && (this.getCommodity() == null ? other.getCommodity() == null : this.getCommodity().equals(other.getCommodity()))
            && (this.getMarketTradableRestriction() == null ? other.getMarketTradableRestriction() == null : this.getMarketTradableRestriction().equals(other.getMarketTradableRestriction()))
            && (this.getMarketMarketableRestriction() == null ? other.getMarketMarketableRestriction() == null : this.getMarketMarketableRestriction().equals(other.getMarketMarketableRestriction()))
            && (this.getQuantity() == null ? other.getQuantity() == null : this.getQuantity().equals(other.getQuantity()))
            && (this.getAcquisitionTime() == null ? other.getAcquisitionTime() == null : this.getAcquisitionTime().equals(other.getAcquisitionTime()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getAppid() == null) ? 0 : getAppid().hashCode());
        result = prime * result + ((getClassid() == null) ? 0 : getClassid().hashCode());
        result = prime * result + ((getInstanceid() == null) ? 0 : getInstanceid().hashCode());
        result = prime * result + ((getIconUrl() == null) ? 0 : getIconUrl().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getMarketName() == null) ? 0 : getMarketName().hashCode());
        result = prime * result + ((getMarketHashName() == null) ? 0 : getMarketHashName().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getTradable() == null) ? 0 : getTradable().hashCode());
        result = prime * result + ((getCommodity() == null) ? 0 : getCommodity().hashCode());
        result = prime * result + ((getMarketTradableRestriction() == null) ? 0 : getMarketTradableRestriction().hashCode());
        result = prime * result + ((getMarketMarketableRestriction() == null) ? 0 : getMarketMarketableRestriction().hashCode());
        result = prime * result + ((getQuantity() == null) ? 0 : getQuantity().hashCode());
        result = prime * result + ((getAcquisitionTime() == null) ? 0 : getAcquisitionTime().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", appid=").append(appid);
        sb.append(", classid=").append(classid);
        sb.append(", instanceid=").append(instanceid);
        sb.append(", iconUrl=").append(iconUrl);
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", marketName=").append(marketName);
        sb.append(", marketHashName=").append(marketHashName);
        sb.append(", description=").append(description);
        sb.append(", tradable=").append(tradable);
        sb.append(", commodity=").append(commodity);
        sb.append(", marketTradableRestriction=").append(marketTradableRestriction);
        sb.append(", marketMarketableRestriction=").append(marketMarketableRestriction);
        sb.append(", quantity=").append(quantity);
        sb.append(", acquisitionTime=").append(acquisitionTime);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }
}