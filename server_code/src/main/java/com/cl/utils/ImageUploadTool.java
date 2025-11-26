package com.cl.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.cl.config.OSSConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片批量上传工具
 * 用于将本地存储的图片批量上传到云存储
 */
@Component
public class ImageUploadTool {
    
    @Autowired
    private OSS ossClient;
    
    @Autowired
    private OSSConfig ossConfig;
    
    /**
     * 批量上传本地图片到云存储
     * @return 上传结果统计
     */
    public UploadResult batchUploadImages() {
        UploadResult result = new UploadResult();
        
        try {
            // 获取本地文件存储路径
            String localPath = getLocalFilePath();
            if (localPath == null) {
                result.setErrorMessage("无法找到本地文件存储路径");
                return result;
            }
            
            File fileDir = new File(localPath);
            if (!fileDir.exists() || !fileDir.isDirectory()) {
                result.setErrorMessage("本地文件目录不存在: " + localPath);
                return result;
            }
            
            // 获取所有图片文件
            List<File> imageFiles = getImageFiles(fileDir);
            result.setTotalFiles(imageFiles.size());
            
            System.out.println("找到 " + imageFiles.size() + " 个图片文件，开始上传...");
            
            // 批量上传
            for (File imageFile : imageFiles) {
                try {
                    String cloudUrl = uploadSingleImage(imageFile);
                    if (cloudUrl != null) {
                        result.addSuccessFile(imageFile.getName(), cloudUrl);
                        System.out.println("上传成功: " + imageFile.getName() + " -> " + cloudUrl);
                    } else {
                        result.addFailedFile(imageFile.getName(), "上传失败");
                        System.err.println("上传失败: " + imageFile.getName());
                    }
                } catch (Exception e) {
                    result.addFailedFile(imageFile.getName(), e.getMessage());
                    System.err.println("上传异常: " + imageFile.getName() + " - " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            result.setErrorMessage("批量上传过程中发生异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * 上传单个图片文件
     * @param imageFile 图片文件
     * @return 云存储URL，失败返回null
     */
    private String uploadSingleImage(File imageFile) {
        try {
            // 构建云存储对象键
            String objectKey = "uploads/" + imageFile.getName();
            
            // 检查文件是否已存在
            if (ossClient.doesObjectExist(ossConfig.getBucketName(), objectKey)) {
                System.out.println("文件已存在，跳过: " + imageFile.getName());
                return ossConfig.getDomain() + "/" + objectKey;
            }
            
            // 设置文件元数据
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(imageFile.length());
            
            // 根据文件扩展名设置Content-Type
            String fileName = imageFile.getName().toLowerCase();
            if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                metadata.setContentType("image/jpeg");
            } else if (fileName.endsWith(".png")) {
                metadata.setContentType("image/png");
            } else if (fileName.endsWith(".gif")) {
                metadata.setContentType("image/gif");
            } else if (fileName.endsWith(".webp")) {
                metadata.setContentType("image/webp");
            } else {
                metadata.setContentType("application/octet-stream");
            }
            
            // 上传文件
            FileInputStream inputStream = new FileInputStream(imageFile);
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                ossConfig.getBucketName(), objectKey, inputStream, metadata
            );
            
            ossClient.putObject(putObjectRequest);
            inputStream.close();
            
            // 返回访问URL
            return ossConfig.getDomain() + "/" + objectKey;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 获取本地文件存储路径
     */
    private String getLocalFilePath() {
        try {
            String pathStr = ResourceUtils.getURL("classpath:static").getPath();
            pathStr = URLDecoder.decode(pathStr, "UTF-8");
            File path = new File(pathStr);
            if (!path.exists()) {
                path = new File("");
            }
            return path.getAbsolutePath() + File.separator + "file";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 递归获取目录下的所有图片文件
     */
    private List<File> getImageFiles(File directory) {
        List<File> imageFiles = new ArrayList<>();
        
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // 递归处理子目录
                        imageFiles.addAll(getImageFiles(file));
                    } else if (isImageFile(file)) {
                        imageFiles.add(file);
                    }
                }
            }
        }
        
        return imageFiles;
    }
    
    /**
     * 判断是否为图片文件
     */
    private boolean isImageFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".jpg") || 
               fileName.endsWith(".jpeg") || 
               fileName.endsWith(".png") || 
               fileName.endsWith(".gif") || 
               fileName.endsWith(".webp") ||
               fileName.endsWith(".bmp");
    }
    
    /**
     * 上传结果统计类
     */
    public static class UploadResult {
        private int totalFiles = 0;
        private List<String> successFiles = new ArrayList<>();
        private List<String> successUrls = new ArrayList<>();
        private List<String> failedFiles = new ArrayList<>();
        private List<String> failedReasons = new ArrayList<>();
        private String errorMessage;
        
        public void addSuccessFile(String fileName, String url) {
            successFiles.add(fileName);
            successUrls.add(url);
        }
        
        public void addFailedFile(String fileName, String reason) {
            failedFiles.add(fileName);
            failedReasons.add(reason);
        }
        
        public int getSuccessCount() {
            return successFiles.size();
        }
        
        public int getFailedCount() {
            return failedFiles.size();
        }
        
        public void printSummary() {
            System.out.println("\n=== 上传结果统计 ===");
            System.out.println("总文件数: " + totalFiles);
            System.out.println("成功上传: " + getSuccessCount());
            System.out.println("上传失败: " + getFailedCount());
            
            if (errorMessage != null) {
                System.out.println("错误信息: " + errorMessage);
            }
            
            if (!failedFiles.isEmpty()) {
                System.out.println("\n失败文件列表:");
                for (int i = 0; i < failedFiles.size(); i++) {
                    System.out.println("  " + failedFiles.get(i) + " - " + failedReasons.get(i));
                }
            }
            
            System.out.println("==================\n");
        }
        
        // Getters and Setters
        public int getTotalFiles() { return totalFiles; }
        public void setTotalFiles(int totalFiles) { this.totalFiles = totalFiles; }
        
        public List<String> getSuccessFiles() { return successFiles; }
        public List<String> getSuccessUrls() { return successUrls; }
        public List<String> getFailedFiles() { return failedFiles; }
        public List<String> getFailedReasons() { return failedReasons; }
        
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }
}