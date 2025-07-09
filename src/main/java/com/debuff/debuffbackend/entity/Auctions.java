package com.debuff.debuffbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 拍卖信息表
 * @TableName auctions
 */
@TableName(value ="auctions")
@Data
public class Auctions {
    /**
     * 拍卖ID，主键
     */
    @TableId(type = IdType.AUTO)
    private Integer auctionId;

    /**
     * 商品ID，外键关联Items表的item_id
     */
    private Integer itemId;

    /**
     * 卖家ID，外键关联Users表的user_id
     */
    private Integer sellerId;

    /**
     * 起始价格
     */
    private BigDecimal startPrice;

    /**
     * 当前最高出价
     */
    private BigDecimal currentBid;

    /**
     * 拍卖结束时间
     */
    private Date endTime;

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
        Auctions other = (Auctions) that;
        return (this.getAuctionId() == null ? other.getAuctionId() == null : this.getAuctionId().equals(other.getAuctionId()))
            && (this.getItemId() == null ? other.getItemId() == null : this.getItemId().equals(other.getItemId()))
            && (this.getSellerId() == null ? other.getSellerId() == null : this.getSellerId().equals(other.getSellerId()))
            && (this.getStartPrice() == null ? other.getStartPrice() == null : this.getStartPrice().equals(other.getStartPrice()))
            && (this.getCurrentBid() == null ? other.getCurrentBid() == null : this.getCurrentBid().equals(other.getCurrentBid()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getAuctionId() == null) ? 0 : getAuctionId().hashCode());
        result = prime * result + ((getItemId() == null) ? 0 : getItemId().hashCode());
        result = prime * result + ((getSellerId() == null) ? 0 : getSellerId().hashCode());
        result = prime * result + ((getStartPrice() == null) ? 0 : getStartPrice().hashCode());
        result = prime * result + ((getCurrentBid() == null) ? 0 : getCurrentBid().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", auctionId=").append(auctionId);
        sb.append(", itemId=").append(itemId);
        sb.append(", sellerId=").append(sellerId);
        sb.append(", startPrice=").append(startPrice);
        sb.append(", currentBid=").append(currentBid);
        sb.append(", endTime=").append(endTime);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }
}