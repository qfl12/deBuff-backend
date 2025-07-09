package com.debuff.debuffbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 管理员操作记录表
 * @TableName adminactions
 */
@TableName(value ="adminactions")
@Data
public class Adminactions {
    /**
     * 操作ID，主键，自动递增
     */
    @TableId(type = IdType.AUTO)
    private Integer actionId;

    /**
     * 管理员ID，外键关联Users表的user_id
     */
    private Integer adminId;

    /**
     * 操作类型
     */
    private Object actionType;

    /**
     * 目标类型
     */
    private Object targetType;

    /**
     * 目标ID
     */
    private Integer targetId;

    /**
     * 详细信息
     */
    private Object details;

    /**
     * 操作时间，默认为当前时间戳
     */
    private Date actionTime;

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
        Adminactions other = (Adminactions) that;
        return (this.getActionId() == null ? other.getActionId() == null : this.getActionId().equals(other.getActionId()))
            && (this.getAdminId() == null ? other.getAdminId() == null : this.getAdminId().equals(other.getAdminId()))
            && (this.getActionType() == null ? other.getActionType() == null : this.getActionType().equals(other.getActionType()))
            && (this.getTargetType() == null ? other.getTargetType() == null : this.getTargetType().equals(other.getTargetType()))
            && (this.getTargetId() == null ? other.getTargetId() == null : this.getTargetId().equals(other.getTargetId()))
            && (this.getDetails() == null ? other.getDetails() == null : this.getDetails().equals(other.getDetails()))
            && (this.getActionTime() == null ? other.getActionTime() == null : this.getActionTime().equals(other.getActionTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getActionId() == null) ? 0 : getActionId().hashCode());
        result = prime * result + ((getAdminId() == null) ? 0 : getAdminId().hashCode());
        result = prime * result + ((getActionType() == null) ? 0 : getActionType().hashCode());
        result = prime * result + ((getTargetType() == null) ? 0 : getTargetType().hashCode());
        result = prime * result + ((getTargetId() == null) ? 0 : getTargetId().hashCode());
        result = prime * result + ((getDetails() == null) ? 0 : getDetails().hashCode());
        result = prime * result + ((getActionTime() == null) ? 0 : getActionTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", actionId=").append(actionId);
        sb.append(", adminId=").append(adminId);
        sb.append(", actionType=").append(actionType);
        sb.append(", targetType=").append(targetType);
        sb.append(", targetId=").append(targetId);
        sb.append(", details=").append(details);
        sb.append(", actionTime=").append(actionTime);
        sb.append("]");
        return sb.toString();
    }
}