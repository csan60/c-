package com.cl.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云OSS配置类
 */
@Configuration
public class OSSConfig {
    
    @Value("${aliyun.oss.endpoint:https://oss-cn-hangzhou.aliyuncs.com}")
    private String endpoint;
    
    @Value("${aliyun.oss.accessKeyId:your-access-key-id}")
    private String accessKeyId;
    
    @Value("${aliyun.oss.accessKeySecret:your-access-key-secret}")
    private String accessKeySecret;
    
    @Value("${aliyun.oss.bucketName:your-bucket-name}")
    private String bucketName;
    
    @Value("${aliyun.oss.domain:https://your-bucket-name.oss-cn-hangzhou.aliyuncs.com}")
    private String domain;
    
    @Bean
    public OSS ossClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
    
    public String getEndpoint() {
        return endpoint;
    }
    
    public String getAccessKeyId() {
        return accessKeyId;
    }
    
    public String getAccessKeySecret() {
        return accessKeySecret;
    }
    
    public String getBucketName() {
        return bucketName;
    }
    
    public String getDomain() {
        return domain;
    }
}