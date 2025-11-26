package com.cl.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("medical_plan")
public class MedicalPlan implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联患者ID，对应older_users表的id字段
     */
    @TableField("older_user_id")
    private Long olderUserId; // 这个字段对应HuanzheEntity的id

    @TableField("drug_name")
    private String drugName;

    private String dosage;

    private String usage;

    private String frequency;

    @TableField("start_date")
    private LocalDate startDate;

    @TableField("end_date")
    private LocalDate endDate;

    private String remarks;

    @TableField("plan_status")
    private Integer planStatus; // 1=有效，0=已结束或作废

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}