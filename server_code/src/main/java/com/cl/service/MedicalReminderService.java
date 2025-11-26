package com.cl.service;

import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.Reminder;
import java.util.List;

public interface MedicalReminderService extends IService<Reminder> {
    
    /**
     * 为医嘱计划创建提醒
     */
    List<Reminder> createRemindersForMedicalPlan(Long medicalPlanId);
    
    /**
     * 获取用户今日的服药提醒
     */
    List<Reminder> getTodayReminders(Long olderUserId);
    
    /**
     * 标记提醒为已完成
     */
    boolean markReminderCompleted(Long reminderId);
    
    /**
     * 获取用户未完成的提醒
     */
    List<Reminder> getPendingReminders(Long olderUserId);
}