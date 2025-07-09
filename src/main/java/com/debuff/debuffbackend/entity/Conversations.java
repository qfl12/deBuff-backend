package com.debuff.debuffbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 会话管理表
 * @TableName conversations
 */
@TableName(value ="conversations")
@Data
public class Conversations {
    /**
     * 会话ID，主键
     */
    @TableId(type = IdType.AUTO)
    private Integer conversationId;

    /**
     * 参与者1
     */
    private Integer user1Id;

    /**
     * 参与者2
     */
    private Integer user2Id;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 最后更新时间
     */
    private Date updatedTime;

    /**
     * 未读消息总数
     */
    private Integer unreadCount;

    /**
     * 最后一条消息
     */
    private Integer lastMessageId;

    /**
     * 会话类型
     */
    private Object conversationType;

    /**
     * 会话状态
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
        Conversations other = (Conversations) that;
        return (this.getConversationId() == null ? other.getConversationId() == null : this.getConversationId().equals(other.getConversationId()))
            && (this.getUser1Id() == null ? other.getUser1Id() == null : this.getUser1Id().equals(other.getUser1Id()))
            && (this.getUser2Id() == null ? other.getUser2Id() == null : this.getUser2Id().equals(other.getUser2Id()))
            && (this.getCreatedTime() == null ? other.getCreatedTime() == null : this.getCreatedTime().equals(other.getCreatedTime()))
            && (this.getUpdatedTime() == null ? other.getUpdatedTime() == null : this.getUpdatedTime().equals(other.getUpdatedTime()))
            && (this.getUnreadCount() == null ? other.getUnreadCount() == null : this.getUnreadCount().equals(other.getUnreadCount()))
            && (this.getLastMessageId() == null ? other.getLastMessageId() == null : this.getLastMessageId().equals(other.getLastMessageId()))
            && (this.getConversationType() == null ? other.getConversationType() == null : this.getConversationType().equals(other.getConversationType()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getConversationId() == null) ? 0 : getConversationId().hashCode());
        result = prime * result + ((getUser1Id() == null) ? 0 : getUser1Id().hashCode());
        result = prime * result + ((getUser2Id() == null) ? 0 : getUser2Id().hashCode());
        result = prime * result + ((getCreatedTime() == null) ? 0 : getCreatedTime().hashCode());
        result = prime * result + ((getUpdatedTime() == null) ? 0 : getUpdatedTime().hashCode());
        result = prime * result + ((getUnreadCount() == null) ? 0 : getUnreadCount().hashCode());
        result = prime * result + ((getLastMessageId() == null) ? 0 : getLastMessageId().hashCode());
        result = prime * result + ((getConversationType() == null) ? 0 : getConversationType().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", conversationId=").append(conversationId);
        sb.append(", user1Id=").append(user1Id);
        sb.append(", user2Id=").append(user2Id);
        sb.append(", createdTime=").append(createdTime);
        sb.append(", updatedTime=").append(updatedTime);
        sb.append(", unreadCount=").append(unreadCount);
        sb.append(", lastMessageId=").append(lastMessageId);
        sb.append(", conversationType=").append(conversationType);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }
}