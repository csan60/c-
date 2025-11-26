package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cl.entity.HealthRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface HealthDao extends BaseMapper<HealthRecord> {
    
    /**
     * 根据患者ID获取最新的健康记录
     */
    HealthRecord getLatestByHuanzheId(@Param("huanzheId") Long huanzheId);
    
    /**
     * 根据患者ID和时间范围查询健康记录
     */
    List<HealthRecord> getByHuanzheIdAndDateRange(
        @Param("huanzheId") Long huanzheId,
        @Param("startDate") Date startDate,
        @Param("endDate") Date endDate
    );
    
    /**
     * 获取患者今日步数统计
     */
    Integer getTodaySteps(@Param("huanzheId") Long huanzheId);
    
    /**
     * 获取患者本周平均心率
     */
    Double getWeeklyAvgHeartRate(@Param("huanzheId") Long huanzheId);
}
