package com.debuff.debuffbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 消息记录表
 * @TableName messages
 */
@TableName(value ="messages")
@Data
public class Messages {
    /**
     * 消息ID，主键，自动递增
     */
    @TableId(type = IdType.AUTO)
    private Integer messageId;

    /**
     * 会话ID，外键关联Conversations表
     */
    private Integer conversationId;

    /**
     * 发送者ID，外键关联Users表的user_id
     */
    private Integer fromUserId;

    /**
     * 回复的消息ID，外键关联本表message_id
     */
    private Integer replyToId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发送时间
     */
    private Date messageTime;

    /**
     * 消息内容类型
     */
    private Object messageType;

    /**
     * 消息状态
     */
    private Object status;

    /**
     * 关联商品ID
     */
    private Integer itemId;

    /**
     * 扩展数据(位置、商品快照等)
     */
    private Object extraData;

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
        Messages other = (Messages) that;
        return (this.getMessageId() == null ? other.getMessageId() == null : this.getMessageId().equals(other.getMessageId()))
            && (this.getConversationId() == null ? other.getConversationId() == null : this.getConversationId().equals(other.getConversationId()))
            && (this.getFromUserId() == null ? other.getFromUserId() == null : this.getFromUserId().equals(other.getFromUserId()))
            && (this.getReplyToId() == null ? other.getReplyToId() == null : this.getReplyToId().equals(other.getReplyToId()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getMessageTime() == null ? other.getMessageTime() == null : this.getMessageTime().equals(other.getMessageTime()))
            && (this.getMessageType() == null ? other.getMessageType() == null : this.getMessageType().equals(other.getMessageType()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getItemId() == null ? other.getItemId() == null : this.getItemId().equals(other.getItemId()))
            && (this.getExtraData() == null ? other.getExtraData() == null : this.getExtraData().equals(other.getExtraData()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMessageId() == null) ? 0 : getMessageId().hashCode());
        result = prime * result + ((getConversationId() == null) ? 0 : getConversationId().hashCode());
        result = prime * result + ((getFromUserId() == null) ? 0 : getFromUserId().hashCode());
        result = prime * result + ((getReplyToId() == null) ? 0 : getReplyToId().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getMessageTime() == null) ? 0 : getMessageTime().hashCode());
        result = prime * result + ((getMessageType() == null) ? 0 : getMessageType().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getItemId() == null) ? 0 : getItemId().hashCode());
        result = prime * result + ((getExtraData() == null) ? 0 : getExtraData().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", messageId=").append(messageId);
        sb.append(", conversationId=").append(conversationId);
        sb.append(", fromUserId=").append(fromUserId);
        sb.append(", replyToId=").append(replyToId);
        sb.append(", content=").append(content);
        sb.append(", messageTime=").append(messageTime);
        sb.append(", messageType=").append(messageType);
        sb.append(", status=").append(status);
        sb.append(", itemId=").append(itemId);
        sb.append(", extraData=").append(extraData);
        sb.append("]");
        return sb.toString();
    }
}