package com.debuff.debuffbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 租赁信息表
 * @TableName rentals
 */
@TableName(value ="rentals")
@Data
public class Rentals {
    /**
     * 租赁ID，主键
     */
    @TableId(type = IdType.AUTO)
    private Integer rentalId;

    /**
     * 商品ID，外键关联Items表的item_id
     */
    private Integer itemId;

    /**
     * 租用者ID，外键关联Users表的user_id
     */
    private Integer renterId;

    /**
     * 所有者ID，外键关联Users表的user_id
     */
    private Integer ownerId;

    /**
     * 租金
     */
    private BigDecimal rentalPrice;

    /**
     * 租赁开始日期
     */
    private Date startDate;

    /**
     * 租赁结束日期
     */
    private Date endDate;

    /**
     * 状态：进行中、已完成、已取消
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
        Rentals other = (Rentals) that;
        return (this.getRentalId() == null ? other.getRentalId() == null : this.getRentalId().equals(other.getRentalId()))
            && (this.getItemId() == null ? other.getItemId() == null : this.getItemId().equals(other.getItemId()))
            && (this.getRenterId() == null ? other.getRenterId() == null : this.getRenterId().equals(other.getRenterId()))
            && (this.getOwnerId() == null ? other.getOwnerId() == null : this.getOwnerId().equals(other.getOwnerId()))
            && (this.getRentalPrice() == null ? other.getRentalPrice() == null : this.getRentalPrice().equals(other.getRentalPrice()))
            && (this.getStartDate() == null ? other.getStartDate() == null : this.getStartDate().equals(other.getStartDate()))
            && (this.getEndDate() == null ? other.getEndDate() == null : this.getEndDate().equals(other.getEndDate()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRentalId() == null) ? 0 : getRentalId().hashCode());
        result = prime * result + ((getItemId() == null) ? 0 : getItemId().hashCode());
        result = prime * result + ((getRenterId() == null) ? 0 : getRenterId().hashCode());
        result = prime * result + ((getOwnerId() == null) ? 0 : getOwnerId().hashCode());
        result = prime * result + ((getRentalPrice() == null) ? 0 : getRentalPrice().hashCode());
        result = prime * result + ((getStartDate() == null) ? 0 : getStartDate().hashCode());
        result = prime * result + ((getEndDate() == null) ? 0 : getEndDate().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", rentalId=").append(rentalId);
        sb.append(", itemId=").append(itemId);
        sb.append(", renterId=").append(renterId);
        sb.append(", ownerId=").append(ownerId);
        sb.append(", rentalPrice=").append(rentalPrice);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }
}