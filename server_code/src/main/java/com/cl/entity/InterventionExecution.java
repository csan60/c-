package com.cl.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

@TableName("intervention_execution")
public class InterventionExecution implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("huanzhe_id")
    private Long huanzheId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("execution_date")
    private Date executionDate; // 记录/生成日期

    private String source; // 来源：intervention/assessment/manual

    @TableField("plan_json")
    private String planJson; // AI生成方案的JSON全文

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("created_at")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("updated_at")
    private Date updatedAt;

    public InterventionExecution() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getHuanzheId() { return huanzheId; }
    public void setHuanzheId(Long huanzheId) { this.huanzheId = huanzheId; }

    public Date getExecutionDate() { return executionDate; }
    public void setExecutionDate(Date executionDate) { this.executionDate = executionDate; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getPlanJson() { return planJson; }
    public void setPlanJson(String planJson) { this.planJson = planJson; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}