package com.cl.entity;


import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("health_record")
public class HealthRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long huanzheId;                    // 患者ID
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date recordDate;                   // 记录日期
    
    private BigDecimal heightCm;               // 身高(cm)
    private BigDecimal weightKg;               // 体重(kg)
    private BigDecimal bmi;                    // BMI指数
    private BigDecimal temperature;            // 体温(°C)
    private Integer systolic;                  // 收缩压
    private Integer diastolic;                 // 舒张压
    private Integer heartRate;                 // 心率
    private BigDecimal bloodOxygen;            // 血氧饱和度
    private BigDecimal bloodSugar;             // 血糖
    private BigDecimal bodyFat;                // 体脂率
    private BigDecimal bloodLipids;            // 血脂
    private Integer stepsToday;                // 今日步数
    private BigDecimal caloriesBurned;         // 消耗卡路里
    private String remark;                     // 备注
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;                   // 记录创建时间

    // 构造函数
    public HealthRecord() {}

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHuanzheId() {
        return huanzheId;
    }

    public void setHuanzheId(Long huanzheId) {
        this.huanzheId = huanzheId;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public BigDecimal getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(BigDecimal heightCm) {
        this.heightCm = heightCm;
    }

    public BigDecimal getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(BigDecimal weightKg) {
        this.weightKg = weightKg;
    }

    public BigDecimal getBmi() {
        return bmi;
    }

    public void setBmi(BigDecimal bmi) {
        this.bmi = bmi;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public Integer getSystolic() {
        return systolic;
    }

    public void setSystolic(Integer systolic) {
        this.systolic = systolic;
    }

    public Integer getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(Integer diastolic) {
        this.diastolic = diastolic;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    public BigDecimal getBloodOxygen() {
        return bloodOxygen;
    }

    public void setBloodOxygen(BigDecimal bloodOxygen) {
        this.bloodOxygen = bloodOxygen;
    }

    public BigDecimal getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(BigDecimal bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public BigDecimal getBodyFat() {
        return bodyFat;
    }

    public void setBodyFat(BigDecimal bodyFat) {
        this.bodyFat = bodyFat;
    }

    public BigDecimal getBloodLipids() {
        return bloodLipids;
    }

    public void setBloodLipids(BigDecimal bloodLipids) {
        this.bloodLipids = bloodLipids;
    }

    public Integer getStepsToday() {
        return stepsToday;
    }

    public void setStepsToday(Integer stepsToday) {
        this.stepsToday = stepsToday;
    }

    public BigDecimal getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(BigDecimal caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
