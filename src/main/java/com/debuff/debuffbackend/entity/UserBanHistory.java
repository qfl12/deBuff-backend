package com.debuff.debuffbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 记录用户的封禁历史信息
 * @TableName user_ban_history
 */
@TableName(value ="user_ban_history")
@Data
public class UserBanHistory {
    /**
     * 唯一标识一条封禁历史记录
     */
    @TableId(type = IdType.AUTO)
    private Integer historyId;

    /**
     * 与被封禁用户的用户ID相关联
     */
    private Integer userId;

    /**
     * 封禁的开始时间
     */
    private Date banStart;

    /**
     * 封禁的预计结束时间
     */
    private Date banEnd;

    /**
     * 实际解封的时间，若未解封则为NULL
     */
    private Date unbanTime;

    /**
     * 解封的方式，0-未解封, 1-自动解封, 2-管理员解封
     */
    private Integer unbanType;

    /**
     * 如果是由管理员手动解封，则需要填写解封的原因
     */
    private String adminUnbanReason;

    /**
     * 封禁的原因说明
     */
    private String reason;

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
        UserBanHistory other = (UserBanHistory) that;
        return (this.getHistoryId() == null ? other.getHistoryId() == null : this.getHistoryId().equals(other.getHistoryId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getBanStart() == null ? other.getBanStart() == null : this.getBanStart().equals(other.getBanStart()))
            && (this.getBanEnd() == null ? other.getBanEnd() == null : this.getBanEnd().equals(other.getBanEnd()))
            && (this.getUnbanTime() == null ? other.getUnbanTime() == null : this.getUnbanTime().equals(other.getUnbanTime()))
            && (this.getUnbanType() == null ? other.getUnbanType() == null : this.getUnbanType().equals(other.getUnbanType()))
            && (this.getAdminUnbanReason() == null ? other.getAdminUnbanReason() == null : this.getAdminUnbanReason().equals(other.getAdminUnbanReason()))
            && (this.getReason() == null ? other.getReason() == null : this.getReason().equals(other.getReason()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getHistoryId() == null) ? 0 : getHistoryId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getBanStart() == null) ? 0 : getBanStart().hashCode());
        result = prime * result + ((getBanEnd() == null) ? 0 : getBanEnd().hashCode());
        result = prime * result + ((getUnbanTime() == null) ? 0 : getUnbanTime().hashCode());
        result = prime * result + ((getUnbanType() == null) ? 0 : getUnbanType().hashCode());
        result = prime * result + ((getAdminUnbanReason() == null) ? 0 : getAdminUnbanReason().hashCode());
        result = prime * result + ((getReason() == null) ? 0 : getReason().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", historyId=").append(historyId);
        sb.append(", userId=").append(userId);
        sb.append(", banStart=").append(banStart);
        sb.append(", banEnd=").append(banEnd);
        sb.append(", unbanTime=").append(unbanTime);
        sb.append(", unbanType=").append(unbanType);
        sb.append(", adminUnbanReason=").append(adminUnbanReason);
        sb.append(", reason=").append(reason);
        sb.append("]");
        return sb.toString();
    }
}