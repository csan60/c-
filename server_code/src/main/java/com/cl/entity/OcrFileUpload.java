package com.cl.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("ocr_file_upload")
public class OcrFileUpload {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("older_user_id")
    private Long olderUserId;

    @TableField("medical_plan_id")
    private Long medicalPlanId;

    @TableField("original_filename")
    private String originalFilename;

    @TableField("file_path")
    private String filePath;

    @TableField("file_size")
    private Long fileSize;

    @TableField("ocr_result")
    private String ocrResult;

    @TableField("upload_time")
    private LocalDateTime uploadTime;

    @TableField("type")
    private String type; // 文件类型

    @TableField("ocr_status")
    private String ocrStatus; // 识别状态



}