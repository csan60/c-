package com.cl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 统一管理讯飞 ASR + TTS 配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "ai")
public class AiConfig {

    private XunFei xunfei;

    @Data
    public static class XunFei {
        // 通用配置（ASR + TTS 一起用）
        private String appId;
        private String apiKey;
        private String apiSecret;

        // ASR 地址
        private String asrUrl;

        // TTS 地址（你需要自己填）
        private String ttsUrl;
    }

}
