package com.cl.service;

import com.cl.entity.ChatMessage;
import com.cl.entity.ChatMessageResponse;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatSessionService {

    // 存每个 session 的聊天记录
    private final Map<String, List<ChatMessageResponse>> sessionMap = new HashMap<>();

    /** 获取某个 session 的历史消息 */
    public List<ChatMessageResponse> getHistory(String sessionId) {
        return sessionMap.getOrDefault(sessionId, new ArrayList<>());
    }

    /** 在 session 中追加一条消息 */
    public void appendMessage(String sessionId, ChatMessageResponse msg) {
        sessionMap.computeIfAbsent(sessionId, k -> new ArrayList<>());
        sessionMap.get(sessionId).add(msg);
    }
}
