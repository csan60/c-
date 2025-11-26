package com.cl.entity;



import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("drug_plan")
public class DrugPlan {

    private Long id;

    @TableField("medical_plan_id")
    private Long medicalPlanId;

    @TableField("drug_name")
    private String drugName;

    private String dosage;

    private String frequency;

    private String times; // JSON array string

    private LocalDateTime addtime;
}
