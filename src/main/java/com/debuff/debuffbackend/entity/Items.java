package com.debuff.debuffbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 商品信息表
 * @TableName items
 */
@TableName(value ="items")
@Data
public class Items {
    /**
     * 商品ID，主键，自动递增
     */
    @TableId(type = IdType.AUTO)
    private Integer itemId;

    /**
     * 卖家ID，外键关联Users表的user_id
     */
    private Integer sellerId;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商品分类ID，外键关联Categories表的category_id
     */
    private Integer categoryId;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 商品图片URL
     */
    private String imageUrl;

    /**
     * 磨损值（可为空）
     */
    private String wearValue;

    /**
     * 冷却期结束时间
     */
    private Date coolDown;

    /**
     * 发布时间，默认为当前时间戳
     */
    private Date postTime;

    /**
     * 是否已售出，默认否
     */
    private Integer isSold;

    /**
     * 商品状态：在售、已售完、已移除，默认在售
     */
    private Object status;

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
        return (this.getItemId() == null ? other.getItemId() == null : this.getItemId().equals(other.getItemId()))
            && (this.getSellerId() == null ? other.getSellerId() == null : this.getSellerId().equals(other.getSellerId()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getCategoryId() == null ? other.getCategoryId() == null : this.getCategoryId().equals(other.getCategoryId()))
            && (this.getPrice() == null ? other.getPrice() == null : this.getPrice().equals(other.getPrice()))
            && (this.getImageUrl() == null ? other.getImageUrl() == null : this.getImageUrl().equals(other.getImageUrl()))
            && (this.getWearValue() == null ? other.getWearValue() == null : this.getWearValue().equals(other.getWearValue()))
            && (this.getCoolDown() == null ? other.getCoolDown() == null : this.getCoolDown().equals(other.getCoolDown()))
            && (this.getPostTime() == null ? other.getPostTime() == null : this.getPostTime().equals(other.getPostTime()))
            && (this.getIsSold() == null ? other.getIsSold() == null : this.getIsSold().equals(other.getIsSold()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getItemId() == null) ? 0 : getItemId().hashCode());
        result = prime * result + ((getSellerId() == null) ? 0 : getSellerId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getCategoryId() == null) ? 0 : getCategoryId().hashCode());
        result = prime * result + ((getPrice() == null) ? 0 : getPrice().hashCode());
        result = prime * result + ((getImageUrl() == null) ? 0 : getImageUrl().hashCode());
        result = prime * result + ((getWearValue() == null) ? 0 : getWearValue().hashCode());
        result = prime * result + ((getCoolDown() == null) ? 0 : getCoolDown().hashCode());
        result = prime * result + ((getPostTime() == null) ? 0 : getPostTime().hashCode());
        result = prime * result + ((getIsSold() == null) ? 0 : getIsSold().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", itemId=").append(itemId);
        sb.append(", sellerId=").append(sellerId);
        sb.append(", title=").append(title);
        sb.append(", description=").append(description);
        sb.append(", categoryId=").append(categoryId);
        sb.append(", price=").append(price);
        sb.append(", imageUrl=").append(imageUrl);
        sb.append(", wearValue=").append(wearValue);
        sb.append(", coolDown=").append(coolDown);
        sb.append(", postTime=").append(postTime);
        sb.append(", isSold=").append(isSold);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }
}