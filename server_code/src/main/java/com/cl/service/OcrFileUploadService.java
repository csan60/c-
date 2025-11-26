package com.cl.service;

import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.OcrFileUpload;
import com.cl.utils.PageUtils;

import java.util.Map;

public interface OcrFileUploadService extends IService<OcrFileUpload> {
    
    PageUtils queryPage(Map<String, Object> params);
}