package com.cl.service.impl;



import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.dao.HealthDao;
import com.cl.entity.HealthRecord;
import com.cl.service.DeepSeekAIService;
import com.cl.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class HealthServiceImpl extends ServiceImpl<HealthDao, HealthRecord> implements HealthService {
    
    @Autowired
    private HealthDao healthDao;
    
    @Autowired
    private DeepSeekAIService deepSeekAIService;
    
    private static final Logger log = LoggerFactory.getLogger(HealthServiceImpl.class);
    
    @Override
    public boolean saveOrUpdateWithCalculation(HealthRecord healthRecord) {
        // 自动计算BMI
        if (healthRecord.getHeightCm() != null && healthRecord.getWeightKg() != null) {
            BigDecimal bmi = calculateBMI(healthRecord.getHeightCm(), healthRecord.getWeightKg());
            healthRecord.setBmi(bmi);
        }
        
        // 自动计算卡路里消耗（基于步数的简单计算）
        if (healthRecord.getStepsToday() != null) {
            BigDecimal calories = calculateCalories(healthRecord.getStepsToday());
            healthRecord.setCaloriesBurned(calories);
        }
        
        // 设置记录时间
        if (healthRecord.getRecordDate() == null) {
            healthRecord.setRecordDate(new Date());
        }
        
        // 针对同一用户的更新逻辑
        if (healthRecord.getHuanzheId() != null) {
            // 查找该用户是否已有健康记录
            HealthRecord existingRecord = healthDao.getLatestByHuanzheId(healthRecord.getHuanzheId());
            
            if (existingRecord != null) {
                // 如果存在记录，更新现有记录
                healthRecord.setId(existingRecord.getId());
                // 保留原有的创建时间
                if (healthRecord.getCreateTime() == null) {
                    healthRecord.setCreateTime(existingRecord.getCreateTime());
                }
            } else {
                // 如果不存在记录，设置创建时间
                if (healthRecord.getCreateTime() == null) {
                    healthRecord.setCreateTime(new Date());
                }
            }
        } else {
            // 设置创建时间
            if (healthRecord.getCreateTime() == null) {
                healthRecord.setCreateTime(new Date());
            }
        }
        
        return this.insertOrUpdate(healthRecord);
    }
    
    @Override
    public HealthRecord getLatestByHuanzheId(Long huanzheId) {
        return healthDao.getLatestByHuanzheId(huanzheId);
    }

    @Override
    public List<HealthRecord> getByHuanzheIdAndDateRange(Long huanzheId,Date startDate,Date endDate) {
        return healthDao.getByHuanzheIdAndDateRange(huanzheId, startDate, endDate);
    }
    

    @Override
    public Map<String, Object> getHealthStats(Long huanzheId) {
        Map<String, Object> stats = new HashMap<>();
        
        // 获取今日步数
        Integer todaySteps = healthDao.getTodaySteps(huanzheId);
        stats.put("todaySteps", todaySteps != null ? todaySteps : 0);
        
        // 获取本周平均心率
        Double avgHeartRate = healthDao.getWeeklyAvgHeartRate(huanzheId);
        stats.put("weeklyAvgHeartRate", avgHeartRate != null ? avgHeartRate : 0.0);
        
        // 获取最新健康记录
        HealthRecord latest = getLatestByHuanzheId(huanzheId);
        if (latest != null) {
            stats.put("latestBMI", latest.getBmi());
            stats.put("latestBloodPressure", 
                latest.getSystolic() + "/" + latest.getDiastolic());
            stats.put("latestHeartRate", latest.getHeartRate());
        }
        
        return stats;
    }
    
    @Override
    public Map<String, Object> getHealthTrend(Long huanzheId) {
        Map<String, Object> trend = new HashMap<>();
        
        // 获取最近7天的数据
        Calendar cal = Calendar.getInstance();
        Date endDate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, -7);
        Date startDate = cal.getTime();
        
        List<HealthRecord> records = getByHuanzheIdAndDateRange(huanzheId, startDate, endDate);
        
        // 分析趋势
        if (!records.isEmpty()) {
            // 体重趋势
            List<BigDecimal> weights = new ArrayList<>();
            List<Integer> heartRates = new ArrayList<>();
            
            for (HealthRecord record : records) {
                if (record.getWeightKg() != null) {
                    weights.add(record.getWeightKg());
                }
                if (record.getHeartRate() != null) {
                    heartRates.add(record.getHeartRate());
                }
            }
            
            trend.put("weightTrend", analyzeTrend(weights));
            trend.put("heartRateTrend", analyzeIntegerTrend(heartRates));
        }
        
        return trend;
    }
    
    @Override
    public Map<String, Object> checkHealthAbnormal(HealthRecord healthRecord) {
        Map<String, Object> result = new HashMap<>();
        List<String> warnings = new ArrayList<>();
        
        // BMI异常检测
        if (healthRecord.getBmi() != null) {
            String bmiStatus = getBMIStatus(healthRecord.getBmi());
            if (!"正常".equals(bmiStatus)) {
                warnings.add("BMI状态：" + bmiStatus);
            }
        }
        
        // 血压异常检测
        if (healthRecord.getSystolic() != null && healthRecord.getDiastolic() != null) {
            if (healthRecord.getSystolic() > 140 || healthRecord.getDiastolic() > 90) {
                warnings.add("血压偏高，建议就医检查");
            } else if (healthRecord.getSystolic() < 90 || healthRecord.getDiastolic() < 60) {
                warnings.add("血压偏低，请注意休息");
            }
        }
        
        // 心率异常检测
        if (healthRecord.getHeartRate() != null) {
            if (healthRecord.getHeartRate() > 100) {
                warnings.add("心率偏快，建议休息");
            } else if (healthRecord.getHeartRate() < 60) {
                warnings.add("心率偏慢，如有不适请就医");
            }
        }
        
        result.put("hasAbnormal", !warnings.isEmpty());
        result.put("warnings", warnings);
        
        return result;
    }
    
    // 私有辅助方法
    private BigDecimal calculateBMI(BigDecimal heightCm, BigDecimal weightKg) {
        BigDecimal heightM = heightCm.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal bmi = weightKg.divide(heightM.multiply(heightM), 2, RoundingMode.HALF_UP);
        return bmi;
    }
    
    private BigDecimal calculateCalories(Integer steps) {
        // 简单计算：每步约消耗0.04卡路里
        return new BigDecimal(steps * 0.04).setScale(2, RoundingMode.HALF_UP);
    }
    
    private String getBMIStatus(BigDecimal bmi) {
        double bmiValue = bmi.doubleValue();
        if (bmiValue < 18.5) {
            return "偏瘦";
        } else if (bmiValue < 24) {
            return "正常";
        } else if (bmiValue < 28) {
            return "偏胖";
        } else {
            return "肥胖";
        }
    }
    
    private String analyzeTrend(List<BigDecimal> values) {
        if (values.size() < 2) return "数据不足";
        
        BigDecimal first = values.get(0);
        BigDecimal last = values.get(values.size() - 1);
        
        if (last.compareTo(first) > 0) {
            return "上升";
        } else if (last.compareTo(first) < 0) {
            return "下降";
        } else {
            return "稳定";
        }
    }
    
    private String analyzeIntegerTrend(List<Integer> values) {
        if (values.size() < 2) return "数据不足";
        
        Integer first = values.get(0);
        Integer last = values.get(values.size() - 1);
        
        if (last > first) {
            return "上升";
        } else if (last < first) {
            return "下降";
        } else {
            return "稳定";
        }
    }
    


    

}
