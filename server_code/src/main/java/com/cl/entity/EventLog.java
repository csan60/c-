package com.cl.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("event_logs")
public class EventLog implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id; // 主键

    private Long userId; // 关联用户ID

    private String type; // 事件类型（如 求助、健康提醒、异常检测）

    private String description; // 事件详情描述

    private LocalDateTime time; // 事件发生时间
}
