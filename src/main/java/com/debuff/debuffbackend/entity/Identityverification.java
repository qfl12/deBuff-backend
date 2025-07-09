package com.debuff.debuffbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 实名认证信息表
 * @TableName identityverification
 */
@TableName(value ="identityverification")
@Data
public class Identityverification {
    /**
     * 主键，自动递增
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 外键，关联到用户表中的`user_id`
     */
    private Integer userId;

    /**
     * 真实姓名
     */
    private String name;

    /**
     * 身份证号或其他身份证明编号（加密存储）
     */
    private String idNumber;

    /**
     * 其他可能的验证资料
     */
    private String verificationData;

    /**
     * 认证通过的时间
     */
    private Date verifiedAt;

    /**
     * 审核状态，默认是待审核
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
        Identityverification other = (Identityverification) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getIdNumber() == null ? other.getIdNumber() == null : this.getIdNumber().equals(other.getIdNumber()))
            && (this.getVerificationData() == null ? other.getVerificationData() == null : this.getVerificationData().equals(other.getVerificationData()))
            && (this.getVerifiedAt() == null ? other.getVerifiedAt() == null : this.getVerifiedAt().equals(other.getVerifiedAt()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getIdNumber() == null) ? 0 : getIdNumber().hashCode());
        result = prime * result + ((getVerificationData() == null) ? 0 : getVerificationData().hashCode());
        result = prime * result + ((getVerifiedAt() == null) ? 0 : getVerifiedAt().hashCode());
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
        sb.append(", name=").append(name);
        sb.append(", idNumber=").append(idNumber);
        sb.append(", verificationData=").append(verificationData);
        sb.append(", verifiedAt=").append(verifiedAt);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }
}