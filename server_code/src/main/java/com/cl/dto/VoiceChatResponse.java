package com.cl.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VoiceChatResponse {

    private String userText;
    private String assistantText;
    private String assistantAudioUrl; // 先预留，将来接 TTS

    public VoiceChatResponse() {
    }

    public VoiceChatResponse(String userText, String assistantText, String assistantAudioUrl) {
        this.userText = userText;
        this.assistantText = assistantText;
        this.assistantAudioUrl = assistantAudioUrl;
    }

}
