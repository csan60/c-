package com.cl.service;



import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.HealthRecord;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface HealthService extends IService<HealthRecord> {
    
    /**
     * 保存或更新健康记录（自动计算BMI等字段）
     */
    boolean saveOrUpdateWithCalculation(HealthRecord healthRecord);
    
    /**
     * 根据患者ID获取最新健康记录
     */
    HealthRecord getLatestByHuanzheId(Long huanzheId);
    
    /**
     * 根据患者ID和时间范围查询健康记录
     */
    List<HealthRecord> getByHuanzheIdAndDateRange(Long huanzheId, Date startDate, Date endDate);
    
    /**
     * 获取患者健康数据统计
     */
    Map<String, Object> getHealthStats(Long huanzheId);
    
    /**
     * 获取患者健康趋势数据（最近7天）
     */
    Map<String, Object> getHealthTrend(Long huanzheId);
    
    /**
     * 健康数据异常检测
     */
    Map<String, Object> checkHealthAbnormal(HealthRecord healthRecord);
    

}
