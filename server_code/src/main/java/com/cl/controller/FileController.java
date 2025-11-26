package com.cl.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.config.OSSConfig;
import com.cl.entity.ConfigEntity;
import com.cl.service.ConfigService;
import com.cl.utils.OSSUtil;
import com.cl.utils.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 上传文件映射表
 */
@RestController
@RequestMapping("file")
@SuppressWarnings({"unchecked", "rawtypes"})
public class FileController {
    @Autowired
    private ConfigService configService;
    
    @Autowired
    private OSSUtil ossUtil;
    
    @Autowired
    private OSS ossClient;
    
    @Autowired
    private OSSConfig ossConfig;
    
    @Value("${file.storage.type:oss}")
    private String storageType; // 默认使用oss

    /**
     * 上传文件到OSS云存储
     * 文件大小限制：300MB
     */
    @RequestMapping("/upload")
    @IgnoreAuth
    public R upload(@RequestParam("file") MultipartFile file, String type) {
        try {
            // 参数验证
            if (file == null || file.isEmpty()) {
                return R.error("上传文件不能为空");
            }
            
            // 文件大小检查（300MB = 314572800 bytes）
            long maxSize = 314572800L; // 300MB
            if (file.getSize() > maxSize) {
                return R.error("文件大小超过限制，最大支持300MB");
            }
            
            // 文件名安全检查
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.trim().isEmpty()) {
                return R.error("文件名不能为空");
            }
            
            // 检查文件扩展名
            if (!originalFilename.contains(".")) {
                return R.error("文件必须包含扩展名");
            }
            
            String fileExt = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String fileName;
            String fileUrl;
            
            // 使用OSS云存储
            try {
                if (StringUtils.isNotBlank(type) && type.contains("_template")) {
                    // 模板文件上传到templates目录
                    fileName = type + "." + fileExt;
                    fileUrl = ossUtil.uploadFile(file, "templates", fileName);
                } else {
                    // 普通文件上传到uploads目录
                    fileUrl = ossUtil.uploadFile(file, "uploads");
                    // 从URL中提取文件名
                    fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
                }
            } catch (IOException e) {
                return R.error("文件上传到OSS云存储失败: " + e.getMessage());
            }
            
            // 处理特殊类型文件的配置更新
            if (StringUtils.isNotBlank(type) && type.equals("1")) {
                try {
                    ConfigEntity configEntity = configService.selectOne(new EntityWrapper<ConfigEntity>().eq("name", "faceFile"));
                    if (configEntity == null) {
                        configEntity = new ConfigEntity();
                        configEntity.setName("faceFile");
                        configEntity.setValue(fileUrl); // OSS存储直接使用完整URL
                    } else {
                        configEntity.setValue(fileUrl); // OSS存储直接使用完整URL
                    }
                    configService.insertOrUpdate(configEntity);
                } catch (Exception e) {
                    // 配置更新失败不影响文件上传结果
                    System.err.println("更新配置失败: " + e.getMessage());
                }
            }
            
            return R.ok().put("file", fileUrl).put("fileName", fileName).put("url", fileUrl);
            
        } catch (Exception e) {
            return R.error("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 从OSS云存储下载文件
     */
    @IgnoreAuth
    @RequestMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam String fileName) {
        try {
            // 参数验证
            if (StringUtils.isBlank(fileName)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            // 构建OSS对象键
            String objectKey;
            if (fileName.startsWith("uploads/") || fileName.startsWith("templates/")) {
                objectKey = fileName; // 已包含路径前缀
            } else {
                objectKey = "uploads/" + fileName; // 默认在uploads目录下查找
            }
            
            // 检查文件是否存在
            if (!ossClient.doesObjectExist(ossConfig.getBucketName(), objectKey)) {
                // 尝试在templates目录下查找
                objectKey = "templates/" + fileName;
                if (!ossClient.doesObjectExist(ossConfig.getBucketName(), objectKey)) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }
            
            // 从OSS下载文件
            OSSObject ossObject = ossClient.getObject(ossConfig.getBucketName(), objectKey);
            InputStream inputStream = ossObject.getObjectContent();
            
            // 读取文件内容到字节数组
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            byte[] fileBytes = outputStream.toByteArray();
            
            // 关闭流
            inputStream.close();
            outputStream.close();
            ossObject.close();
            
            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentLength(fileBytes.length);
            
            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
