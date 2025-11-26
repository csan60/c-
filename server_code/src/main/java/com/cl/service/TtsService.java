package com.cl.service;

import com.cl.config.AiConfig;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Service
public class TtsService {

    @Autowired
    private AiConfig aiConfig;

    private final OkHttpClient client = new OkHttpClient();

    public byte[] synthesize(String text) throws Exception {
        AiConfig.XunFei config = aiConfig.getXunfei();
        String appId = config.getAppId();
        String apiKey = config.getApiKey();
        String apiSecret = config.getApiSecret();
        String url = config.getTtsUrl();   // ← 这里现在完全正确，不会报错

        String curTime = String.valueOf(System.currentTimeMillis() / 1000);

        String paramJson =
                "{\"aue\":\"lame\",\"voice_name\":\"xiaoyan\",\"speed\":\"50\",\"volume\":\"50\",\"pitch\":\"50\",\"engine_type\":\"intp65\"}";

        String paramBase64 = Base64.getEncoder()
                .encodeToString(paramJson.getBytes(StandardCharsets.UTF_8));

        String checkSum = md5(apiKey + curTime + paramBase64);

        RequestBody body = new FormBody.Builder()
                .add("text", text)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-Appid", appId)
                .addHeader("X-CurTime", curTime)
                .addHeader("X-Param", paramBase64)
                .addHeader("X-CheckSum", checkSum)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new RuntimeException("TTS请求失败：" + response);
        }

        return response.body().bytes(); // 返回 MP3 字节流
    }

    private String md5(String text) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] arr = md.digest(text.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : arr) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
