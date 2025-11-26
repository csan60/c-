package com.cl.service;

import com.cl.config.DeepSeekConfig;
import com.cl.entity.ChatMessage;
import com.cl.entity.ChatMessageResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LlmService {

    @Autowired
    private DeepSeekConfig config;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public String chat(String sessionId, String userText, List<ChatMessageResponse> history) throws Exception {

        // 🔹 拼接消息列表
        StringBuilder messagesJson = new StringBuilder("[");
        for (ChatMessageResponse msg : history) {
            messagesJson.append("{\"role\":\"")
                    .append(msg.getRole())
                    .append("\",\"content\":\"")
                    .append(escape(msg.getContent()))
                    .append("\"},");
        }
        messagesJson.append("{\"role\":\"user\",\"content\":\"")
                .append(escape(userText))
                .append("\"}]");

        // 🔹 构造请求体
        String bodyJson = "{"
                + "\"model\":\"" + config.getModel() + "\","
                + "\"messages\":" + messagesJson
                + "}";

        // 🔹 创建请求
        Request request = new Request.Builder()
                .url(config.getApiUrl())
                .addHeader("Authorization", "Bearer " + config.getApiKey())
                .post(RequestBody.create(bodyJson, MediaType.parse("application/json")))
                .build();

        // 🔹 发起请求
        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            return "我现在有点忙，请稍后再试试～";
        }

        // 🔹 解析返回
        JsonNode root = mapper.readTree(response.body().string());
        return root.get("choices").get(0).get("message").get("content").asText();
    }

    private String escape(String text) {
        return text.replace("\"", "\\\"");
    }
}
