package com.cl.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatMessageResponse {

    private String role; // "user" 或 "assistant"
    private String content;

    public ChatMessageResponse() {}

    public ChatMessageResponse(String role, String content) {
        this.role = role;
        this.content = content;
    }

}
