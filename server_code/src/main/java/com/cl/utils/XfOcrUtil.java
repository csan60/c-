package com.cl.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class XfOcrUtil {

    private static final String APP_ID = "359c2cb8";
    private static final String API_KEY = "8e0e674afe071c0f848d9236833680d6";
    private static final String API_SECRET = "ZDg0OTgzYmE5NmZiNmExODhhNzhlZjlh";
    private static final String OCR_URL = "https://api.xf-yun.com/v1/private/sf8e6aca1";

    public static String ocrImage(String imageBase64) throws Exception {
        String host = "api.xf-yun.com";
        String date = getGMTDate();

        // 构建 signature 原始字段
        String requestLine = "POST /v1/private/sf8e6aca1 HTTP/1.1";
        String signatureOrigin = "host: " + host + "\n" + "date: " + date + "\n" + requestLine;

        // 生成 signature
        String signatureSha = HmacUtil.hmacSha256(signatureOrigin, API_SECRET);

        // 构建 Authorization 原始字符串
        String authOrigin = String.format("api_key=\"%s\", algorithm=\"hmac-sha256\", headers=\"host date request-line\", signature=\"%s\"", API_KEY, signatureSha);
        String authorization = Base64.getEncoder().encodeToString(authOrigin.getBytes(StandardCharsets.UTF_8));

        // 拼接完整 URL
        String url = OCR_URL
                + "?authorization=" + URLEncoder.encode(authorization, "UTF-8")
                + "&host=" + host
                + "&date=" + URLEncoder.encode(date, "UTF-8");

        // 构建请求体
        Map<String, Object> header = new HashMap<>();
        header.put("app_id", APP_ID);
        header.put("status", 3);

        Map<String, Object> result = new HashMap<>();
        result.put("encoding", "utf8");
        result.put("compress", "raw");
        result.put("format", "json");

        Map<String, Object> innerParam = new HashMap<>();
        innerParam.put("category", "ch_en_public_cloud");
        innerParam.put("result", result);

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("sf8e6aca1", innerParam);

        Map<String, Object> imageMap = new HashMap<>();
        imageMap.put("encoding", "jpg");
        imageMap.put("status", 3);
        imageMap.put("image", imageBase64);

        Map<String, Object> payload = new HashMap<>();
        payload.put("sf8e6aca1_data_1", imageMap);

        Map<String, Object> body = new HashMap<>();
        body.put("header", header);
        body.put("parameter", parameter);
        body.put("payload", payload);

        // 发起 POST 请求
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json");
        post.setEntity(new StringEntity(JSON.toJSONString(body), StandardCharsets.UTF_8));

        HttpResponse response = httpClient.execute(post);
        String jsonResponse = EntityUtils.toString(response.getEntity(), "UTF-8");
        return jsonResponse;
    }

    private static String getGMTDate() {
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return format.format(new Date());
    }
}
