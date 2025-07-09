package com.debuff.debuffbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 用户库存信息表
 * @TableName userinventory
 */
@TableName(value ="userinventory")
@Data
public class Userinventory {
    /**
     * 库存ID，主键，自动递增
     */
    @TableId(type = IdType.AUTO)
    private Integer inventoryId;

    /**
     * 用户ID，外键关联Users表的user_id
     */
    private Integer userId;

    /**
     * 商品ID，外键关联Items表的item_id
     */
    private Integer itemId;

    /**
     * 数量，默认为1
     */
    private Integer quantity;

    /**
     * 获取时间，默认为当前时间戳
     */
    private Date acquisitionTime;

    /**
     * 状态：可用、在售、交易中、已删除，默认可用
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
        Userinventory other = (Userinventory) that;
        return (this.getInventoryId() == null ? other.getInventoryId() == null : this.getInventoryId().equals(other.getInventoryId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getItemId() == null ? other.getItemId() == null : this.getItemId().equals(other.getItemId()))
            && (this.getQuantity() == null ? other.getQuantity() == null : this.getQuantity().equals(other.getQuantity()))
            && (this.getAcquisitionTime() == null ? other.getAcquisitionTime() == null : this.getAcquisitionTime().equals(other.getAcquisitionTime()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getInventoryId() == null) ? 0 : getInventoryId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getItemId() == null) ? 0 : getItemId().hashCode());
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
        sb.append(", inventoryId=").append(inventoryId);
        sb.append(", userId=").append(userId);
        sb.append(", itemId=").append(itemId);
        sb.append(", quantity=").append(quantity);
        sb.append(", acquisitionTime=").append(acquisitionTime);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }
}