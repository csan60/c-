package com.cl.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class XfyunOcrUtil {
    
    /**
     * 讯飞OCR文字识别
     * @param hostUrl 接口地址
     * @param appId 应用ID
     * @param apiKey API Key
     * @param apiSecret API Secret
     * @param imageBase64 图片Base64编码
     * @return 识别结果文本
     */
    public static String recognizeText(String hostUrl, String appId, String apiKey, 
                                     String apiSecret, String imageBase64) {
        try {
            URL url = new URL(hostUrl);
            
            // 生成RFC1123格式的时间戳
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            String date = format.format(new Date());
            
            // 构建请求体
            JSONObject business = new JSONObject();
            business.put("language", "cn|en");
            business.put("location", "true");
            
            JSONObject common = new JSONObject();
            common.put("app_id", appId);
            
            JSONObject data = new JSONObject();
            data.put("image", imageBase64);
            
            JSONObject param = new JSONObject();
            param.put("common", common);
            param.put("business", business);
            param.put("data", data);
            
            // 生成签名
            String signature = generateSignature(hostUrl, date, param.toString(), apiSecret);
            
            // 构建Authorization header
            String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"",
                    apiKey, "hmac-sha256", "host date request-line", signature);
            
            // 发送HTTP请求
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(hostUrl);
            
            // 设置请求头
            httpPost.setHeader("Content-Type", "application/json; charset=utf-8");
            httpPost.setHeader("Accept", "application/json,version=1.0");
            httpPost.setHeader("Host", url.getHost());
            httpPost.setHeader("Date", date);
            httpPost.setHeader("Authorization", authorization);
            
            // 设置请求体
            StringEntity entity = new StringEntity(param.toString(), StandardCharsets.UTF_8);
            httpPost.setEntity(entity);
            
            // 执行请求
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            String result = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            
            // 解析响应
            return parseOcrResult(result);
            
        } catch (Exception e) {
            log.error("讯飞OCR识别失败", e);
            return "OCR识别失败: " + e.getMessage();
        }
    }
    
    /**
     * 生成签名
     */
    private static String generateSignature(String hostUrl, String date, String body, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        String requestLine = "POST " + url.getPath() + " HTTP/1.1";
        String signatureOrigin = "host: " + url.getHost() + "\n";
        signatureOrigin += "date: " + date + "\n";
        signatureOrigin += requestLine;
        
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);
        byte[] hexDigits = mac.doFinal(signatureOrigin.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(hexDigits);
    }
    
    /**
     * 解析OCR识别结果
     */
    private static String parseOcrResult(String jsonResult) {
        try {
            JSONObject result = JSON.parseObject(jsonResult);
            
            if (result.getInteger("code") != 0) {
                log.error("讯飞OCR识别失败: {}", result.getString("message"));
                return "识别失败: " + result.getString("message");
            }
            
            JSONObject data = result.getJSONObject("data");
            JSONArray blockArray = data.getJSONArray("block");
            
            StringBuilder textBuilder = new StringBuilder();
            for (int i = 0; i < blockArray.size(); i++) {
                JSONObject block = blockArray.getJSONObject(i);
                JSONArray lineArray = block.getJSONArray("line");
                
                for (int j = 0; j < lineArray.size(); j++) {
                    JSONObject line = lineArray.getJSONObject(j);
                    JSONArray wordArray = line.getJSONArray("word");
                    
                    for (int k = 0; k < wordArray.size(); k++) {
                        JSONObject word = wordArray.getJSONObject(k);
                        textBuilder.append(word.getString("content"));
                    }
                    textBuilder.append("\n");
                }
            }
            
            return textBuilder.toString().trim();
            
        } catch (Exception e) {
            log.error("解析OCR结果失败", e);
            return "解析识别结果失败: " + e.getMessage();
        }
    }
}