package com.cl.entity;



import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("reminder")
public class Reminder {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("older_user_id")
    private Long olderUserId;

    @TableField("medical_plan_id")
    private Long medicalPlanId;

    @TableField("drug_plan_id")
    private Long drugPlanId;

    @TableField("remind_time")
    private LocalDateTime remindTime;

    private String channel; // WECHAT / PUSH / VOICE

    private String status; // WAITING / SENT / FAILED

    private String payload; // JSON

    private LocalDateTime addtime;
}