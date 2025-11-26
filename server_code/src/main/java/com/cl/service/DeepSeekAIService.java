package com.cl.service;

import com.cl.entity.HealthRecord;
import java.util.Map;

public interface DeepSeekAIService {
    

    
    /**
     * 构建健康提示词
     */
//    String buildHealthPrompt(HealthRecord healthRecord);
    
    /**
     * 生成健康评估报告
     */
    Map<String, Object> generateHealthAssessment(HealthRecord healthRecord);
    
    /**
     * 生成健康干预方案
     */
    Map<String, Object> generateInterventionPlan(HealthRecord healthRecord, Map<String, Object> assessment);
    
    /**
     * 老人健康咨询问答（可选带入最近一次健康记录作为上下文）
     * @param question 老人提问内容
     * @param healthRecord 最近一次健康记录，可为空
     * @return 包含 answer、disclaimer、suggestedQuestions 等字段
     */
    Map<String, Object> askHealthQuestion(String question, HealthRecord healthRecord);
}