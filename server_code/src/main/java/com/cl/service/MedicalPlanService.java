package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.MedicalPlan;
import com.cl.utils.PageUtils;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface MedicalPlanService extends IService<MedicalPlan> {
    
    /**
     * 分页查询
     */
    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * 列表查询
     */
    List<MedicalPlan> selectListView(Wrapper<MedicalPlan> wrapper);
    
    /**
     * 查询单个视图
     */
    MedicalPlan selectView(@Param("ew") Wrapper<MedicalPlan> wrapper);
    
    /**
     * 分页查询（带条件）
     */
    PageUtils queryPage(Map<String, Object> params, Wrapper<MedicalPlan> wrapper);
    
    /**
     * 根据OCR识别结果创建医嘱计划
     */
    List<MedicalPlan> createMedicalPlansFromOcr(Long olderUserId, String ocrResult);
    
    /**
     * 获取用户的有效医嘱计划
     */
    List<MedicalPlan> getActivePlans(Long olderUserId);
    
    /**
     * 解析OCR文本为结构化数据
     */
    List<Map<String, String>> parseOcrText(String ocrText);
}