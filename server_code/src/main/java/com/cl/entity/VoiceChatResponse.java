package com.cl.entity;

import lombok.Data;

@Data
public class VoiceChatResponse {

    private String userText;           // 用户识别后的文本
    private String assistantText;      // AI 回答
    private String assistantAudioUrl;  // AI 音频 URL
}
