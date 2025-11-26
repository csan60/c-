package com.cl.service;

/**
 * 语音识别服务。
 * 目前先做一个简单 stub：直接返回固定文本，保证流程能跑通。
 * 之后你按讯飞/腾讯云等 ASR 文档，在这里写真正的 HTTP 调用即可。
 */


import com.cl.config.AiConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.*;
import okio.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 讯飞中英识别大模型（iat.xf-yun.com/v1）—— 可直接运行
 */
@Service
public class AsrService {

    @Autowired
    private AiConfig aiConfig;

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .pingInterval(5, TimeUnit.SECONDS)
            .build();

    /**
     * 识别 mp3 语音（≤60秒）
     */
    public String recognize(byte[] audioBytes) throws Exception {

        AiConfig.XunFei config = aiConfig.getXunfei();

        String appId = config.getAppId();
        String apiKey = config.getApiKey();
        String apiSecret = config.getApiSecret();
        String hostUrl = config.getAsrUrl(); // wss://iat.xf-yun.com/v1

        // ========== 1. 生成 Date（RFC1123 格式） ==========
        String date = getGMTDate();

        // ========== 2. 生成 signature 原文 ==========
        String signatureOrigin = "host: iat.xf-yun.com\n"
                + "date: " + date + "\n"
                + "GET /v1 HTTP/1.1";

        // ========== 3. hmac-sha256 ==========
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signatureSha = mac.doFinal(signatureOrigin.getBytes(StandardCharsets.UTF_8));
        String signature = Base64.getEncoder().encodeToString(signatureSha);

        // ========== 4. authorization_origin ==========
        String authorizationOrigin =
                "api_key=\"" + apiKey + "\", "
                        + "algorithm=\"hmac-sha256\", "
                        + "headers=\"host date request-line\", "
                        + "signature=\"" + signature + "\"";

        String authorization = Base64.getEncoder()
                .encodeToString(authorizationOrigin.getBytes(StandardCharsets.UTF_8));

        // ========== 5. 拼接最终URL ==========
        String url = hostUrl
                + "?authorization=" + URLEncoder.encode(authorization, "UTF-8")
                + "&date=" + URLEncoder.encode(date, "UTF-8")
                + "&host=iat.xf-yun.com";

        // ========== WebSocket 开始 ==========
        CountDownLatch latch = new CountDownLatch(1);
        StringBuilder resultText = new StringBuilder();

        Request request = new Request.Builder().url(url).build();

        WebSocketListener listener = new WebSocketListener() {

            int seq = 1;

            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                // ======== 第一帧：参数帧 ========
                Map<String, Object> frame = new LinkedHashMap<>();

                // header
                Map<String, Object> header = new HashMap<>();
                header.put("app_id", appId);
                header.put("status", 0);

                // parameter
                Map<String, Object> iat = new HashMap<>();
                iat.put("domain", "slm");
                iat.put("language", "zh_cn");
                iat.put("accent", "mandarin");

                Map<String, Object> parameter = new HashMap<>();
                parameter.put("iat", iat);

                // payload
                Map<String, Object> audio = new HashMap<>();
                audio.put("encoding", "raw");
                audio.put("sample_rate", 16000);
                audio.put("channels", 1);
                audio.put("bit_depth", 16);
                audio.put("seq", seq++);
                audio.put("status", 0);
                audio.put("audio", "");

                Map<String, Object> payload = new HashMap<>();
                payload.put("audio", audio);

                frame.put("header", header);
                frame.put("parameter", parameter);
                frame.put("payload", payload);

                try {
                    webSocket.send(mapToJson(frame));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                // ======== 第二帧：音频数据（base64） ========
                String audioBase64 = Base64.getEncoder().encodeToString(audioBytes);

                Map<String, Object> audioFrame = new LinkedHashMap<>();

                Map<String, Object> header2 = new HashMap<>();
                header2.put("app_id", appId);
                header2.put("status", 1);

                Map<String, Object> audio2 = new HashMap<>();
                audio2.put("encoding", "lame");
                audio2.put("sample_rate", 16000);
                audio2.put("channels", 1);
                audio2.put("bit_depth", 16);
                audio2.put("seq", seq++);
                audio2.put("status", 1);
                audio2.put("audio", audioBase64);

                Map<String, Object> payload2 = new HashMap<>();
                payload2.put("audio", audio2);

                audioFrame.put("header", header2);
                audioFrame.put("payload", payload2);

                try {
                    webSocket.send(mapToJson(audioFrame));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                // ======== 第三帧：结束帧 ========
                Map<String, Object> endFrame = new LinkedHashMap<>();
                Map<String, Object> header3 = new HashMap<>();
                header3.put("app_id", appId);
                header3.put("status", 2);

                Map<String, Object> audio3 = new HashMap<>();
                audio3.put("seq", seq++);
                audio3.put("status", 2);
                audio3.put("audio", "");

                Map<String, Object> payload3 = new HashMap<>();
                payload3.put("audio", audio3);

                endFrame.put("header", header3);
                endFrame.put("payload", payload3);

                try {
                    webSocket.send(mapToJson(endFrame));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                // 解析返回的 text 字段
                if (text.contains("\"text\"")) {
                    String t = extractText(text);
                    resultText.append(t);
                }

                if (text.contains("\"status\":2")) {
                    webSocket.close(1000, "finish");
                }
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                latch.countDown();
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                latch.countDown();
            }
        };

        client.newWebSocket(request, listener);

        latch.await();

        return resultText.toString();
    }

    /** 把 Map 转成 JSON 字符串 */
    private String mapToJson(Map<String, Object> map) throws JsonProcessingException {
        return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(map);
    }

    /** 提取中英识别文本内容 */
    private String extractText(String json) {
        try {
            if (!json.contains("\"text\"")) return "";

            // text 是 Base64 编码，需要解码
            String base64 = json.split("\"text\":\"")[1].split("\"")[0];
            byte[] decode = Base64.getDecoder().decode(base64);
            String decodeStr = new String(decode, StandardCharsets.UTF_8);

            // 解析 ws-cw-w
            StringBuilder sb = new StringBuilder();
            String[] arr = decodeStr.split("\"w\":\"");
            for (int i = 1; i < arr.length; i++) {
                sb.append(arr[i].split("\"")[0]);
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    /** GMT 时间 */
    private String getGMTDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(new Date());
    }
}
