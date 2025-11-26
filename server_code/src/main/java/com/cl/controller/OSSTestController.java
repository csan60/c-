package com.cl.controller;

import com.aliyun.oss.OSS;
import com.cl.config.OSSConfig;
import com.cl.utils.OSSUtil;
import com.cl.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * OSS测试控制器
 * 用于诊断OSS配置和连接问题
 */
@RestController
@RequestMapping("/admin/oss-test")
public class OSSTestController {
    
    @Autowired
    private OSSConfig ossConfig;
    
    @Autowired
    private OSS ossClient;
    
    @Autowired
    private OSSUtil ossUtil;
    
    @Value("${file.storage.type:local}")
    private String storageType;
    
    /**
     * 检查OSS配置信息
     */
    @GetMapping("/config")
    public R checkConfig() {
        try {
            Map<String, Object> config = new HashMap<>();
            config.put("storageType", storageType);
            config.put("endpoint", ossConfig.getEndpoint());
            config.put("bucketName", ossConfig.getBucketName());
            config.put("domain", ossConfig.getDomain());
            config.put("accessKeyId", ossConfig.getAccessKeyId().substring(0, 8) + "***"); // 隐藏敏感信息
            
            return R.ok().put("config", config);
        } catch (Exception e) {
            return R.error("获取OSS配置失败: " + e.getMessage());
        }
    }
    
    /**
     * 测试OSS连接
     */
    @GetMapping("/connection")
    public R testConnection() {
        try {
            if (!"oss".equals(storageType)) {
                return R.error("当前存储类型为: " + storageType + "，请设置为oss后重试");
            }
            
            // 测试bucket是否存在
            boolean bucketExists = ossClient.doesBucketExist(ossConfig.getBucketName());
            
            if (bucketExists) {
                return R.ok("OSS连接测试成功，Bucket存在");
            } else {
                return R.error("OSS连接成功，但Bucket不存在: " + ossConfig.getBucketName());
            }
        } catch (Exception e) {
            return R.error("OSS连接测试失败: " + e.getMessage());
        }
    }
    
    /**
     * 测试文件上传
     */
    @PostMapping("/upload")
    public R testUpload(@RequestParam("file") MultipartFile file) {
        try {
            if (!"oss".equals(storageType)) {
                return R.error("当前存储类型为: " + storageType + "，请设置为oss后重试");
            }
            
            if (file.isEmpty()) {
                return R.error("请选择要上传的文件");
            }
            
            // 测试上传
            String fileUrl = ossUtil.uploadFile(file, "test");
            
            Map<String, Object> result = new HashMap<>();
            result.put("fileName", file.getOriginalFilename());
            result.put("fileSize", file.getSize());
            result.put("fileUrl", fileUrl);
            
            return R.ok().put("result", result).put("message", "文件上传测试成功");
            
        } catch (Exception e) {
            return R.error("文件上传测试失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取诊断信息
     */
    @GetMapping("/diagnose")
    public R diagnose() {
        try {
            Map<String, Object> diagnosis = new HashMap<>();
            
            // 1. 检查存储类型配置
            diagnosis.put("storageType", storageType);
            diagnosis.put("isOSSEnabled", "oss".equals(storageType));
            
            // 2. 检查OSS配置
            Map<String, Object> ossConfigInfo = new HashMap<>();
            ossConfigInfo.put("endpoint", ossConfig.getEndpoint());
            ossConfigInfo.put("bucketName", ossConfig.getBucketName());
            ossConfigInfo.put("domain", ossConfig.getDomain());
            ossConfigInfo.put("accessKeyId", ossConfig.getAccessKeyId().substring(0, 8) + "***");
            ossConfigInfo.put("hasAccessKeySecret", ossConfig.getAccessKeySecret() != null && !ossConfig.getAccessKeySecret().isEmpty());
            diagnosis.put("ossConfig", ossConfigInfo);
            
            // 3. 检查Bean注入
            diagnosis.put("ossClientInjected", ossClient != null);
            diagnosis.put("ossUtilInjected", ossUtil != null);
            
            // 4. 检查连接状态
            boolean connectionOk = false;
            String connectionMessage = "";
            try {
                if ("oss".equals(storageType)) {
                    connectionOk = ossClient.doesBucketExist(ossConfig.getBucketName());
                    connectionMessage = connectionOk ? "连接正常，Bucket存在" : "连接正常，但Bucket不存在";
                } else {
                    connectionMessage = "存储类型未设置为OSS";
                }
            } catch (Exception e) {
                connectionMessage = "连接失败: " + e.getMessage();
            }
            diagnosis.put("connectionOk", connectionOk);
            diagnosis.put("connectionMessage", connectionMessage);
            
            // 5. 提供建议
            String suggestion = "";
            if (!"oss".equals(storageType)) {
                suggestion = "请在application.yml中设置 file.storage.type: oss";
            } else if (!connectionOk) {
                suggestion = "请检查OSS配置参数是否正确，特别是endpoint、accessKeyId、accessKeySecret和bucketName";
            } else {
                suggestion = "配置看起来正常，可以尝试上传文件测试";
            }
            diagnosis.put("suggestion", suggestion);
            
            return R.ok().put("diagnosis", diagnosis);
            
        } catch (Exception e) {
            return R.error("诊断过程中发生异常: " + e.getMessage());
        }
    }
}