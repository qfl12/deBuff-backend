package com.debuff.debuffbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import jakarta.persistence.Column;

/**
 * 用户信息表
 * @TableName users
 */
@TableName("users")
@Data
public class Users {
    /**
     * 用户ID，主键，自动递增
     */
    @TableId(type = IdType.AUTO)
    private Integer userId;

    /**
     * 邮箱，唯一
     */
    private String email;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 密码哈希值
     */
    private String password;

    /**
     * 昵称
     */
    private String username;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 性别，默认其他
     */
    private Object gender;

    /**
     * 出生日期
     */
    private Date birthDate;

    /**
     * 自我描述，用户简介信息
     */
    private String bio;

    /**
     * Steam账户的唯一标识符
     */
    private String steamId;

    /**
     * 用户提供的Steam API密钥
     */
    private String apiKey;

    /**
     * 用户的Steam交易链接
     */
    private String tradeLink;

    /**
     * 实名认证状态，默认未认证
     */
    private Integer isVerified;

    /**
     * 注册时间，默认为当前时间戳
     */
    private Date registerTime;

    /**
     * 角色：用户或管理员，默认用户
     */
    @TableField(value = "role", jdbcType = JdbcType.VARCHAR)
    @Column(length = 20)
    private String role;

    /**
     * 状态：激活、禁用、待定，默认待定
     */
    private Object status;

    /**
     * 封禁结束时间
     */
    private Date banEnd;

    /**
     * 账户余额，默认0.00
     */
    private BigDecimal balance;

    /**
     * 最后登录时间
     */
    private Date lastLogin;

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
        Users other = (Users) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
            && (this.getAvatarUrl() == null ? other.getAvatarUrl() == null : this.getAvatarUrl().equals(other.getAvatarUrl()))
            && (this.getGender() == null ? other.getGender() == null : this.getGender().equals(other.getGender()))
            && (this.getBirthDate() == null ? other.getBirthDate() == null : this.getBirthDate().equals(other.getBirthDate()))
            && (this.getBio() == null ? other.getBio() == null : this.getBio().equals(other.getBio()))
            && (this.getSteamId() == null ? other.getSteamId() == null : this.getSteamId().equals(other.getSteamId()))
            && (this.getApiKey() == null ? other.getApiKey() == null : this.getApiKey().equals(other.getApiKey()))
            && (this.getTradeLink() == null ? other.getTradeLink() == null : this.getTradeLink().equals(other.getTradeLink()))
            && (this.getIsVerified() == null ? other.getIsVerified() == null : this.getIsVerified().equals(other.getIsVerified()))
            && (this.getRegisterTime() == null ? other.getRegisterTime() == null : this.getRegisterTime().equals(other.getRegisterTime()))
            && (this.getRole() == null ? other.getRole() == null : this.getRole().equals(other.getRole()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getBanEnd() == null ? other.getBanEnd() == null : this.getBanEnd().equals(other.getBanEnd()))
            && (this.getBalance() == null ? other.getBalance() == null : this.getBalance().equals(other.getBalance()))
            && (this.getLastLogin() == null ? other.getLastLogin() == null : this.getLastLogin().equals(other.getLastLogin()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        result = prime * result + ((getAvatarUrl() == null) ? 0 : getAvatarUrl().hashCode());
        result = prime * result + ((getGender() == null) ? 0 : getGender().hashCode());
        result = prime * result + ((getBirthDate() == null) ? 0 : getBirthDate().hashCode());
        result = prime * result + ((getBio() == null) ? 0 : getBio().hashCode());
        result = prime * result + ((getSteamId() == null) ? 0 : getSteamId().hashCode());
        result = prime * result + ((getApiKey() == null) ? 0 : getApiKey().hashCode());
        result = prime * result + ((getTradeLink() == null) ? 0 : getTradeLink().hashCode());
        result = prime * result + ((getIsVerified() == null) ? 0 : getIsVerified().hashCode());
        result = prime * result + ((getRegisterTime() == null) ? 0 : getRegisterTime().hashCode());
        result = prime * result + ((getRole() == null) ? 0 : getRole().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getBanEnd() == null) ? 0 : getBanEnd().hashCode());
        result = prime * result + ((getBalance() == null) ? 0 : getBalance().hashCode());
        result = prime * result + ((getLastLogin() == null) ? 0 : getLastLogin().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", email=").append(email);
        sb.append(", phone=").append(phone);
        sb.append(", psaaword=").append(password);
        sb.append(", username=").append(username);
        sb.append(", avatarUrl=").append(avatarUrl);
        sb.append(", gender=").append(gender);
        sb.append(", birthDate=").append(birthDate);
        sb.append(", bio=").append(bio);
        sb.append(", steamId=").append(steamId);
        sb.append(", apiKey=").append(apiKey);
        sb.append(", tradeLink=").append(tradeLink);
        sb.append(", isVerified=").append(isVerified);
        sb.append(", registerTime=").append(registerTime);
        sb.append(", role=").append(role);
        sb.append(", status=").append(status);
        sb.append(", banEnd=").append(banEnd);
        sb.append(", balance=").append(balance);
        sb.append(", lastLogin=").append(lastLogin);
        sb.append("]");
        return sb.toString();
    }
}