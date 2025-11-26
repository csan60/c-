package com.cl.controller;

import com.baidu.aip.ocr.AipOcr;
import com.cl.annotation.IgnoreAuth;
import com.cl.utils.R;
//import com.google.common.net.HttpHeaders;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;




@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*", allowedHeaders = "*") // 添加跨域支持
public class AiOcrController {

    private static final String APP_ID = "6984172";
    private static final String API_KEY = "TciROKLKWsMbXSbDKciLncpq";
    private static final String SECRET_KEY = "e3jKO0vAMRQBDRg2CZb7qKvBvp98dGIn";

    private static final AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

    static {
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
    }

    @PostMapping("/upload")
    public R uploadAndProcess(@RequestParam("file") MultipartFile file,
                              @RequestParam(value = "olderUserId", required = false) String olderUserId) throws IOException {
        try {
            byte[] imageBytes = file.getBytes();

            // 设置OCR选项
            HashMap<String, String> options = new HashMap<>();
            options.put("language_type", "CHN_ENG"); // 中英文混合
            options.put("detect_direction", "true"); // 检测图像朝向

            // 直接传二进制
            JSONObject ocrRes = client.basicGeneral(imageBytes, options);
            System.out.println("OCR识别结果: " + ocrRes.toString());

            JSONArray wordsArray = ocrRes.optJSONArray("words_result");
            StringBuilder extractedText = new StringBuilder();
            if (wordsArray != null) {
                for (int i = 0; i < wordsArray.length(); i++) {
                    extractedText.append(wordsArray.getJSONObject(i).getString("words")).append("\n");
                }
            }

            String aiReply = callYourAiModel(extractedText.toString());

            return R.ok()
                    .put("code", 0)
                    .put("extractedText", extractedText.toString())
                    .put("aiReply", aiReply);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error().put("msg", "OCR识别失败: " + e.getMessage());
        }
    }


    @IgnoreAuth
    // 添加健康检查接口
    @GetMapping("/check")
    public R checkHealth() {
        return R.ok().put("status", "UP");
    }

    // 伪代码，自己实现
    private String callYourAiModel(String text) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://api.deepseek.com/v1/chat/completions";
            String apiKey = "sk-f59d2a4ef8024ded821fb32115303c13"; // ✅ 替换成你自己的 Key，别硬编码生产环境

            // 构造请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey); // 设置 Authorization: Bearer {API_KEY}

            // 构造消息体（DeepSeek 要求 Chat 格式）
            JSONObject userMsg = new JSONObject();
            userMsg.put("role", "user");
            userMsg.put("content", text);

            JSONArray messages = new JSONArray();
            messages.put(userMsg);

            // 请求参数
            JSONObject requestJson = new JSONObject();
            requestJson.put("model", "deepseek-chat"); // 或 deepseek-coder，看你想要什么
            requestJson.put("messages", messages);
            requestJson.put("temperature", 0.7);

            HttpEntity<String> entity = new HttpEntity<>(requestJson.toString(), headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject responseJson = new JSONObject(response.getBody());
                JSONArray choices = responseJson.optJSONArray("choices");
                if (choices != null && choices.length() > 0) {
                    JSONObject message = choices.getJSONObject(0).optJSONObject("message");
                    return message != null ? message.optString("content", "无回复") : "无 message 字段";
                } else {
                    return "DeepSeek 返回无内容";
                }
            } else {
                return "DeepSeek 接口错误：" + response.getStatusCodeValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "调用 DeepSeek 出错：" + e.getMessage();
        }
    }
}