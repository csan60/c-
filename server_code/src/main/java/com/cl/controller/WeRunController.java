package com.cl.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/wx")
public class WeRunController {

    @Value("${wx.appid}")
    private String appId;

    @Value("${wx.secret}")
    private String secret;

    private final ObjectMapper mapper = new ObjectMapper();

    // 请求体
    public static class WeRunReq {
        public String code;
        public String encryptedData;
        public String iv;
    }

    // 统一响应
    private Map<String, Object> ok(Object data) {
        Map<String, Object> m = new HashMap<>();
        m.put("code", 0);
        m.put("data", data);
        return m;
    }
    private Map<String, Object> err(String msg) {
        Map<String, Object> m = new HashMap<>();
        m.put("code", 500);
        m.put("msg", msg);
        return m;
    }

    @PostMapping("/decryptWeRun")
    public Map<String, Object> decryptWeRun(@RequestBody WeRunReq req) {
        if (req == null || isBlank(req.code) || isBlank(req.encryptedData) || isBlank(req.iv)) {
            return err("参数不完整");
        }
        try {
            // 1) 用 code 获取 session_key
            String jscode2sessionUrl = String.format(
                    "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                    appId, secret, req.code
            );
            RestTemplate rt = new RestTemplate();
            String ses = rt.getForObject(jscode2sessionUrl, String.class);
            JsonNode sesJson = mapper.readTree(ses);
            if (sesJson == null || sesJson.get("session_key") == null) {
                return err("获取 session_key 失败");
            }
            String sessionKey = sesJson.get("session_key").asText();

            // 2) 解密
            String json = decrypt(sessionKey, req.iv, req.encryptedData);
            if (json == null) {
                return err("解密失败");
            }
            JsonNode root = mapper.readTree(json);
            JsonNode arr = root.get("stepInfoList");

            // 3) 取“今日步数”（stepInfoList 按时间升序，最后一项为当天或最近一天）
            int todayStep = 0;
            if (arr != null && arr.isArray() && arr.size() > 0) {
                JsonNode last = arr.get(arr.size() - 1);
                if (last != null && last.get("step") != null) {
                    todayStep = last.get("step").asInt(0);
                }
            }
            Map<String, Object> data = new HashMap<>();
            data.put("todayStep", todayStep);
            data.put("stepInfoList", mapper.convertValue(arr, List.class));
            return ok(data);
        } catch (Exception e) {
            return err("服务异常，请稍后重试");
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    // AES-128-CBC，PKCS5Padding（对等于PKCS7）
    private String decrypt(String sessionKey, String iv, String encryptedData) {
        try {
            byte[] keyBytes = Base64Utils.decodeFromString(sessionKey);
            byte[] ivBytes = Base64Utils.decodeFromString(iv);
            byte[] dataBytes = Base64Utils.decodeFromString(encryptedData);

            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] result = cipher.doFinal(dataBytes);
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }
}