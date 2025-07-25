package com.debuff.debuffbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 物品标签信息表
 * @TableName item_tags
 */
@TableName(value ="item_tags")
@Data
public class ItemTags {
    /**
     * 自增主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 物品ID（外键）
     */
    private Long itemId;

    /**
     * 标签类别
     */
    private String category;

    /**
     * 内部名称，用于程序识别
     */
    private String internalName;

    /**
     * 本地化后的标签类别名称
     */
    private String localizedCategoryName;

    /**
     * 本地化后的标签名称
     */
    private String localizedTagName;

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
        ItemTags other = (ItemTags) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getItemId() == null ? other.getItemId() == null : this.getItemId().equals(other.getItemId()))
            && (this.getCategory() == null ? other.getCategory() == null : this.getCategory().equals(other.getCategory()))
            && (this.getInternalName() == null ? other.getInternalName() == null : this.getInternalName().equals(other.getInternalName()))
            && (this.getLocalizedCategoryName() == null ? other.getLocalizedCategoryName() == null : this.getLocalizedCategoryName().equals(other.getLocalizedCategoryName()))
            && (this.getLocalizedTagName() == null ? other.getLocalizedTagName() == null : this.getLocalizedTagName().equals(other.getLocalizedTagName()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getItemId() == null) ? 0 : getItemId().hashCode());
        result = prime * result + ((getCategory() == null) ? 0 : getCategory().hashCode());
        result = prime * result + ((getInternalName() == null) ? 0 : getInternalName().hashCode());
        result = prime * result + ((getLocalizedCategoryName() == null) ? 0 : getLocalizedCategoryName().hashCode());
        result = prime * result + ((getLocalizedTagName() == null) ? 0 : getLocalizedTagName().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", itemId=").append(itemId);
        sb.append(", category=").append(category);
        sb.append(", internalName=").append(internalName);
        sb.append(", localizedCategoryName=").append(localizedCategoryName);
        sb.append(", localizedTagName=").append(localizedTagName);
        sb.append("]");
        return sb.toString();
    }
}