package com.cl.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 聊天消息实体
 * 说明：用于患者与子女之间的双向聊天。
 */
@Data
@TableName("chat_message")
public class ChatMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 发送方ID（从会话中获取） */
    private Long senderId;

    /** 发送方表名，例如：huanzhe / zinv */
    private String senderTable;

    /** 接收方ID */
    private Long receiverId;

    /** 接收方表名，例如：huanzhe / zinv */
    private String receiverTable;

    /** 消息内容 */
    private String content;

    /** 消息类型：text/image 等 */
    private String msgType;

    /** 是否已读：0未读 1已读 */
    private Integer isRead;

    /** 创建时间 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date createTime;

    /** 阅读时间（仅接收方阅读时设置） */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date readTime;


}