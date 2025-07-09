package com.debuff.debuffbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 订单信息表
 * @TableName orders
 */
@TableName(value ="orders")
@Data
public class Orders {
    /**
     * 订单ID，主键
     */
    @TableId
    private String orderId;

    /**
     * 买家ID，外键关联Users表的user_id
     */
    private Integer buyerId;

    /**
     * 商品ID，外键关联Items表的item_id
     */
    private Integer itemId;

    /**
     * 购买时间，默认为当前时间戳
     */
    private Date purchaseTime;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 支付方式：余额、支付宝、微信，默认余额
     */
    private Object paymentMethod;

    /**
     * 订单状态：预售、待处理、已支付、已发货、已完成、已取消，默认预售
     */
    private Object status;

    /**
     * 预计发货时间，用于预售商品
     */
    private Date expectedDeliveryTime;

    /**
     * 配送信息
     */
    private Object deliveryInfo;

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
        Orders other = (Orders) that;
        return (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getBuyerId() == null ? other.getBuyerId() == null : this.getBuyerId().equals(other.getBuyerId()))
            && (this.getItemId() == null ? other.getItemId() == null : this.getItemId().equals(other.getItemId()))
            && (this.getPurchaseTime() == null ? other.getPurchaseTime() == null : this.getPurchaseTime().equals(other.getPurchaseTime()))
            && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()))
            && (this.getPaymentMethod() == null ? other.getPaymentMethod() == null : this.getPaymentMethod().equals(other.getPaymentMethod()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getExpectedDeliveryTime() == null ? other.getExpectedDeliveryTime() == null : this.getExpectedDeliveryTime().equals(other.getExpectedDeliveryTime()))
            && (this.getDeliveryInfo() == null ? other.getDeliveryInfo() == null : this.getDeliveryInfo().equals(other.getDeliveryInfo()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getBuyerId() == null) ? 0 : getBuyerId().hashCode());
        result = prime * result + ((getItemId() == null) ? 0 : getItemId().hashCode());
        result = prime * result + ((getPurchaseTime() == null) ? 0 : getPurchaseTime().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
        result = prime * result + ((getPaymentMethod() == null) ? 0 : getPaymentMethod().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getExpectedDeliveryTime() == null) ? 0 : getExpectedDeliveryTime().hashCode());
        result = prime * result + ((getDeliveryInfo() == null) ? 0 : getDeliveryInfo().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", orderId=").append(orderId);
        sb.append(", buyerId=").append(buyerId);
        sb.append(", itemId=").append(itemId);
        sb.append(", purchaseTime=").append(purchaseTime);
        sb.append(", amount=").append(amount);
        sb.append(", paymentMethod=").append(paymentMethod);
        sb.append(", status=").append(status);
        sb.append(", expectedDeliveryTime=").append(expectedDeliveryTime);
        sb.append(", deliveryInfo=").append(deliveryInfo);
        sb.append("]");
        return sb.toString();
    }
}