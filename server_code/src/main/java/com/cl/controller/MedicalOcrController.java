package com.cl.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cl.entity.MedicalPlan;
import com.cl.entity.OcrFileUpload;
import com.cl.service.MedicalPlanService;
import com.cl.service.OcrFileUploadService;
import com.cl.utils.R;
import com.cl.utils.XfOcrUtil;
import com.cl.utils.XfyunOcrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/medical/ocr")
public class MedicalOcrController {
    
    @Autowired
    private MedicalPlanService medicalPlanService;
    
    @Autowired
    private OcrFileUploadService ocrFileUploadService;

    // 讯飞OCR配置
    private static final String XFYUN_HOST_URL = "https://api.xf-yun.com/v1/private/sf8e6aca1";
    private static final String XFYUN_APP_ID = "359c2cb8";
    private static final String XFYUN_API_KEY = "8e0e674afe071c0f848d9236833680d6";
    private static final String XFYUN_API_SECRET = "ZDg0OTgzYmE5NmZiNmExODhhNzhlZjlh";

    /**
     * 上传医嘱图片并进行OCR识别，生成医嘱计划
     */
    @PostMapping("/uploadAndRecognize")
    public R uploadAndRecognize(@RequestParam("file") MultipartFile file,
                               @RequestParam("olderUserId") Long olderUserId,
                               HttpServletRequest request) {
        try {
            // 1. 验证文件
            if (file.isEmpty()) {
                return R.error("请选择要上传的图片文件");
            }
            
            // 验证文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return R.error("只支持图片文件格式");
            }

            // 2. 保存文件到本地
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String uploadDir = "file/medical_ocr/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            File savedFile = new File(dir, fileName);
            file.transferTo(savedFile);
            String filePath = uploadDir + fileName;

            // 3. 将图片转换为Base64
            byte[] imageBytes = Files.readAllBytes(savedFile.toPath());
            String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);

            // 4. 调用讯飞OCR进行识别
            String ocrResult;
            try {
                // 优先使用XfyunOcrUtil
                ocrResult = XfyunOcrUtil.recognizeText(XFYUN_HOST_URL, XFYUN_APP_ID, 
                                                     XFYUN_API_KEY, XFYUN_API_SECRET, imageBase64);
                
                // 如果XfyunOcrUtil失败，尝试使用XfOcrUtil
                if (ocrResult == null || ocrResult.contains("失败")) {
                    log.warn("XfyunOcrUtil识别失败，尝试使用XfOcrUtil");
                    String jsonResponse = XfOcrUtil.ocrImage(imageBase64);
                    ocrResult = parseXfOcrResponse(jsonResponse);
                }
            } catch (Exception e) {
                log.error("OCR识别失败", e);
                return R.error("图片识别失败：" + e.getMessage());
            }

            // 5. 保存OCR文件上传记录
            OcrFileUpload ocrFileUpload = new OcrFileUpload();
            ocrFileUpload.setOlderUserId(olderUserId);
            ocrFileUpload.setFilePath(filePath);
            ocrFileUpload.setOcrResult(ocrResult);
            ocrFileUpload.setUploadTime(LocalDateTime.now());
            ocrFileUploadService.insert(ocrFileUpload);

            // 6. 解析OCR结果并创建医嘱计划
            List<MedicalPlan> medicalPlans = medicalPlanService.createMedicalPlansFromOcr(olderUserId, ocrResult);
            
            // 7. 更新OCR记录的医嘱计划ID（如果需要关联）
            if (!medicalPlans.isEmpty()) {
                ocrFileUpload.setMedicalPlanId(medicalPlans.get(0).getId());
                ocrFileUploadService.updateById(ocrFileUpload);
            }

            return R.ok("医嘱图片识别成功")
                    .put("ocrResult", ocrResult)
                    .put("medicalPlans", medicalPlans)
                    .put("planCount", medicalPlans.size())
                    .put("filePath", filePath);

        } catch (IOException e) {
            log.error("文件上传失败", e);
            return R.error("文件上传失败：" + e.getMessage());
        } catch (Exception e) {
            log.error("医嘱识别处理失败", e);
            return R.error("处理失败：" + e.getMessage());
        }
    }

    /**
     * 仅进行OCR识别，不创建医嘱计划
     */
    @PostMapping("/recognizeOnly")
    public R recognizeOnly(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return R.error("请选择要上传的图片文件");
            }

            // 转换为Base64
            byte[] imageBytes = file.getBytes();
            String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);

            // OCR识别
            String ocrResult = XfyunOcrUtil.recognizeText(XFYUN_HOST_URL, XFYUN_APP_ID, 
                                                        XFYUN_API_KEY, XFYUN_API_SECRET, imageBase64);

            // 解析为结构化数据
            List<Map<String, String>> parsedData = medicalPlanService.parseOcrText(ocrResult);

            return R.ok("识别成功")
                    .put("ocrResult", ocrResult)
                    .put("parsedData", parsedData);

        } catch (Exception e) {
            log.error("OCR识别失败", e);
            return R.error("识别失败：" + e.getMessage());
        }
    }

    /**
     * 根据已有OCR结果创建医嘱计划
     */
    @PostMapping("/createPlansFromText")
    public R createPlansFromText(@RequestParam Long olderUserId, 
                                @RequestParam String ocrText) {
        try {
            List<MedicalPlan> plans = medicalPlanService.createMedicalPlansFromOcr(olderUserId, ocrText);
            return R.ok("医嘱计划创建成功")
                    .put("medicalPlans", plans)
                    .put("count", plans.size());
        } catch (Exception e) {
            log.error("创建医嘱计划失败", e);
            return R.error("创建失败：" + e.getMessage());
        }
    }

    /**
     * 解析XfOcrUtil返回的JSON响应
     */
    private String parseXfOcrResponse(String jsonResponse) {
        try {
            JSONObject response = JSON.parseObject(jsonResponse);
            
            // 检查响应状态
            if (response.containsKey("header")) {
                JSONObject header = response.getJSONObject("header");
                if (header.getInteger("code") != 0) {
                    return "识别失败：" + header.getString("message");
                }
            }
            
            // 提取识别结果
            if (response.containsKey("payload")) {
                JSONObject payload = response.getJSONObject("payload");
                if (payload.containsKey("sf8e6aca1_data_1")) {
                    JSONObject data = payload.getJSONObject("sf8e6aca1_data_1");
                    if (data.containsKey("text")) {
                        String base64Text = data.getString("text");
                        byte[] textBytes = Base64.getDecoder().decode(base64Text);
                        return new String(textBytes, "UTF-8");
                    }
                }
            }
            
            return "无法解析识别结果";
            
        } catch (Exception e) {
            log.error("解析OCR响应失败", e);
            return "解析识别结果失败：" + e.getMessage();
        }
    }
}