package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.ChatMessage;
import com.cl.entity.view.ChatMessageView;
import com.cl.utils.PageUtils;

import java.util.List;
import java.util.Map;

public interface ChatMessageService extends IService<ChatMessage> {
    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Wrapper<ChatMessage> wrapper);

    List<ChatMessageView> selectListView(Wrapper<ChatMessage> wrapper);

    ChatMessageView selectView(Wrapper<ChatMessage> wrapper);

    /**
     * 标记会话中某一方收到的消息为已读
     */
    int markAsRead(Long receiverId, String receiverTable, Long senderId, String senderTable);
}