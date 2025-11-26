package com.cl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.dao.OcrFileUploadDao;
import com.cl.entity.OcrFileUpload;
import com.cl.service.OcrFileUploadService;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("ocrFileUploadService")
public class OcrFileUploadServiceImpl extends ServiceImpl<OcrFileUploadDao, OcrFileUpload> implements OcrFileUploadService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<OcrFileUpload> page = this.selectPage(
                new Query<OcrFileUpload>(params).getPage(),
                new EntityWrapper<OcrFileUpload>()
        );
        return new PageUtils(page);
    }
}