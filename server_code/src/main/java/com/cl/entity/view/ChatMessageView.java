package com.cl.entity.view;

import com.baomidou.mybatisplus.annotations.TableName;
import com.cl.entity.ChatMessage;
import java.io.Serializable;

/**
 * 聊天消息 视图对象
 */
@TableName("chat_message")
public class ChatMessageView extends ChatMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    
}