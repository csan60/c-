package com.cl.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.cl.config.OSSConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 * 阿里云OSS工具类
 */
@Component
public class OSSUtil {
    
    @Autowired
    private OSS ossClient;
    
    @Autowired
    private OSSConfig ossConfig;
    
    /**
     * 上传文件到OSS
     * @param file 文件
     * @param folder 文件夹路径（可选）
     * @return 文件访问URL
     */
    public String uploadFile(MultipartFile file, String folder) throws IOException {
        // 获取文件名和扩展名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        // 生成唯一文件名
        String fileName = UUID.randomUUID().toString().replace("-", "") + "_" + new Date().getTime() + fileExtension;
        
        // 构建文件路径
        String objectKey = folder != null && !folder.isEmpty() ? folder + "/" + fileName : fileName;
        
        // 设置文件元数据
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        
        // 上传文件
        InputStream inputStream = file.getInputStream();
        PutObjectRequest putObjectRequest = new PutObjectRequest(ossConfig.getBucketName(), objectKey, inputStream, metadata);
        ossClient.putObject(putObjectRequest);
        
        // 关闭输入流
        inputStream.close();
        
        // 返回文件访问URL
        return ossConfig.getDomain() + "/" + objectKey;
    }
    
    /**
     * 上传文件到OSS（指定文件名）
     * @param file 文件
     * @param folder 文件夹路径
     * @param customFileName 自定义文件名
     * @return 文件访问URL
     */
    public String uploadFile(MultipartFile file, String folder, String customFileName) throws IOException {
        // 构建文件路径
        String objectKey = folder != null && !folder.isEmpty() ? folder + "/" + customFileName : customFileName;
        
        // 设置文件元数据
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        
        // 上传文件
        InputStream inputStream = file.getInputStream();
        PutObjectRequest putObjectRequest = new PutObjectRequest(ossConfig.getBucketName(), objectKey, inputStream, metadata);
        ossClient.putObject(putObjectRequest);
        
        // 关闭输入流
        inputStream.close();
        
        // 返回文件访问URL
        return ossConfig.getDomain() + "/" + objectKey;
    }
    
    /**
     * 删除OSS文件
     * @param fileUrl 文件URL
     */
    public void deleteFile(String fileUrl) {
        if (fileUrl != null && fileUrl.startsWith(ossConfig.getDomain())) {
            String objectKey = fileUrl.replace(ossConfig.getDomain() + "/", "");
            ossClient.deleteObject(ossConfig.getBucketName(), objectKey);
        }
    }
    
    /**
     * 检查文件是否存在
     * @param fileUrl 文件URL
     * @return 是否存在
     */
    public boolean doesFileExist(String fileUrl) {
        if (fileUrl != null && fileUrl.startsWith(ossConfig.getDomain())) {
            String objectKey = fileUrl.replace(ossConfig.getDomain() + "/", "");
            return ossClient.doesObjectExist(ossConfig.getBucketName(), objectKey);
        }
        return false;
    }
}