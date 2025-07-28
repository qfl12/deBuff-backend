package com.debuff.debuffbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 交易确认表
 * @TableName tradeconfirmations
 */
@TableName(value ="tradeconfirmations")
@Data
public class Tradeconfirmations {
    /**
     * 确认ID，主键
     */
    @TableId(type = IdType.AUTO)
    private Integer confirmationId;

    /**
     * 订单ID，外键关联Orders表的order_id
     */
    private Long orderId;

    /**
     * 买家ID，外键关联Users表的user_id
     */
    private Integer buyerId;

    private Long tradeofferid;

    /**
     * 库存名称
     */
    private String name;

    /**
     * 卖家ID，外键关联Users表的user_id
     */
    private Integer sellerId;

    /**
     * 支付是否确认，默认否
     */
    private Integer paymentConfirmed;

    /**
     * 发货是否确认，默认否
     */
    private Integer shippingConfirmed;

    /**
     * 交易是否完成，默认否
     */
    private Integer tradeCompleted;

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
        Tradeconfirmations other = (Tradeconfirmations) that;
        return (this.getConfirmationId() == null ? other.getConfirmationId() == null : this.getConfirmationId().equals(other.getConfirmationId()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getBuyerId() == null ? other.getBuyerId() == null : this.getBuyerId().equals(other.getBuyerId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getTradeofferid() == null ? other.getTradeofferid() == null : this.getTradeofferid().equals(other.getTradeofferid()))
            && (this.getSellerId() == null ? other.getSellerId() == null : this.getSellerId().equals(other.getSellerId()))
            && (this.getPaymentConfirmed() == null ? other.getPaymentConfirmed() == null : this.getPaymentConfirmed().equals(other.getPaymentConfirmed()))
            && (this.getShippingConfirmed() == null ? other.getShippingConfirmed() == null : this.getShippingConfirmed().equals(other.getShippingConfirmed()))
            && (this.getTradeCompleted() == null ? other.getTradeCompleted() == null : this.getTradeCompleted().equals(other.getTradeCompleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getConfirmationId() == null) ? 0 : getConfirmationId().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getBuyerId() == null) ? 0 : getBuyerId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getTradeofferid() == null) ? 0 : getTradeofferid().hashCode());
        result = prime * result + ((getSellerId() == null) ? 0 : getSellerId().hashCode());
        result = prime * result + ((getPaymentConfirmed() == null) ? 0 : getPaymentConfirmed().hashCode());
        result = prime * result + ((getShippingConfirmed() == null) ? 0 : getShippingConfirmed().hashCode());
        result = prime * result + ((getTradeCompleted() == null) ? 0 : getTradeCompleted().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", confirmationId=").append(confirmationId);
        sb.append(", orderId=").append(orderId);
        sb.append(", buyerId=").append(buyerId);
        sb.append(", name=").append(name);
        sb.append(", tradeofferid=").append(tradeofferid);
        sb.append(", sellerId=").append(sellerId);
        sb.append(", paymentConfirmed=").append(paymentConfirmed);
        sb.append(", shippingConfirmed=").append(shippingConfirmed);
        sb.append(", tradeCompleted=").append(tradeCompleted);
        sb.append("]");
        return sb.toString();
    }

    // 关联字段
    @TableField(exist = false) // 非数据库字段
    private MarketListing marketListing;

    @TableField(exist = false) // 非数据库字段
    private Items item;

}