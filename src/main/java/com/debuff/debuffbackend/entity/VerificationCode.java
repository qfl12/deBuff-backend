package com.debuff.debuffbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

/**
 * 验证码实体类
 * @TableName verification_code
 */
@TableName(value = "verification_code")
@Data
public class VerificationCode {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 验证码
     */
    private String code;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 使用状态：0-未使用，1-已使用
     */
    private Integer status;
}