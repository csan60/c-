package com.cl.controller;

import com.cl.entity.HealthRecord;
import com.cl.service.DeepSeekAIService;
import com.cl.service.HealthService;
import com.cl.entity.InterventionExecution;
import com.cl.service.InterventionExecutionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.Arrays;

import com.cl.annotation.IgnoreAuth;

@RestController
@RequestMapping("/deepseek")
@CrossOrigin
public class DeepSeekController {

    @Autowired
    private DeepSeekAIService deepSeekAIService;

    @Autowired
    private HealthService healthService;

    @Autowired
    private InterventionExecutionService interventionExecutionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 根据患者ID生成今日健康评估
     */
    @IgnoreAuth
    @GetMapping("/assessment/{huanzheId}")
    public Map<String, Object> generateTodayAssessment(@PathVariable("huanzheId") Long huanzheId) {
        Map<String, Object> result = new HashMap<>();
        try {
            HealthRecord latestRecord = healthService.getLatestByHuanzheId(huanzheId);
            if (latestRecord == null) {
                result.put("code", 1);
                result.put("msg", "未找到健康记录");
                return result;
            }

            Map<String, Object> assessmentData = deepSeekAIService.generateHealthAssessment(latestRecord);

            // 重新构造返回数据格式，匹配前端期望
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("assessment", assessmentData);
            responseData.put("score", assessmentData.get("overallScore"));
            responseData.put("riskLevel", assessmentData.get("riskLevel"));
            responseData.put("healthRecord", latestRecord);

            try {
                InterventionExecution exec = new InterventionExecution();
                exec.setHuanzheId(huanzheId);
                exec.setExecutionDate(new Date());
                exec.setSource("assessment");
                String planJson = assessmentData == null ? null : objectMapper.writeValueAsString(assessmentData);
                exec.setPlanJson(planJson);
                exec.setCreatedAt(new Date());
                exec.setUpdatedAt(new Date());
                interventionExecutionService.insert(exec);
                responseData.put("executionId", exec.getId());
            } catch (Exception persistEx) {
            }

            result.put("code", 0);
            result.put("msg", "评估成功");
            result.put("data", responseData);
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", "评估失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 根据患者ID生成健康干预方案
     */
    @IgnoreAuth
    @GetMapping("/intervention/{huanzheId}")
    public Map<String, Object> generateInterventionByPatient(@PathVariable("huanzheId") Long huanzheId) {
        System.out.println("收到干预方案请求，老人ID: " + huanzheId);  // 添加调试日志
        Map<String, Object> result = new HashMap<>();
        try {
            HealthRecord latestRecord = healthService.getLatestByHuanzheId(huanzheId);
            if (latestRecord == null) {
                result.put("code", 1);
                result.put("msg", "未找到健康记录");
                return result;
            }

            // 先生成评估报告
            Map<String, Object> assessment = deepSeekAIService.generateHealthAssessment(latestRecord);
            // 再生成干预方案
            Map<String, Object> intervention = deepSeekAIService.generateInterventionPlan(latestRecord, assessment);

            result.put("code", 0);
            result.put("msg", "生成成功");
            result.put("data", intervention);
        } catch (Exception e) {
            System.err.println("生成干预方案失败: " + e.getMessage());  // 添加错误日志
            e.printStackTrace();
            result.put("code", 1);
            result.put("msg", "生成失败：" + e.getMessage());
        }
        return result;
    }



    // =================== 新增：干预执行记录接口 ===================

    // 创建执行记录
    @IgnoreAuth
    @PostMapping("/intervention/execution")
    public Map<String, Object> createExecution(@RequestBody Map<String, Object> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (body == null || !body.containsKey("huanzheId")) {
                result.put("code", 1);
                result.put("msg", "参数缺失：huanzheId");
                return result;
            }
            Long huanzheId = Long.valueOf(String.valueOf(body.get("huanzheId")));
            InterventionExecution exec = new InterventionExecution();
            exec.setHuanzheId(huanzheId);
            // 兼容多种日期格式
            exec.setExecutionDate(parseFlexibleDate(body.get("date")));
            exec.setSource(String.valueOf(body.getOrDefault("source", "intervention")));
            // plan 作为JSON全文保存
            Object planObj = body.get("plan");
            String planJson = planObj == null ? null : objectMapper.writeValueAsString(planObj);
            exec.setPlanJson(planJson);
            exec.setCreatedAt(new Date());
            exec.setUpdatedAt(new Date());
            interventionExecutionService.insert(exec);

            Map<String, Object> resp = new HashMap<>();
            resp.put("id", exec.getId());
            resp.put("huanzheId", exec.getHuanzheId());
            resp.put("date", exec.getExecutionDate());
            // 兼容前端结构
            Map<String, Object> data = new HashMap<>(body);
            data.put("plan", planObj);
            resp.put("data", data);
            // 同时返回顶层 plan 以增强兼容性
            resp.put("plan", planObj);

            result.put("code", 0);
            result.put("msg", "保存成功");
            result.put("data", resp);
            return result;
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", "保存失败：" + e.getMessage());
            return result;
        }
    }

    // 按患者查询执行记录列表
    @IgnoreAuth
    @GetMapping("/intervention/execution/{huanzheId}")
    public Map<String, Object> listExecutions(@PathVariable("huanzheId") Long huanzheId) {
        Map<String, Object> result = new HashMap<>();
        List<InterventionExecution> list = interventionExecutionService.selectList(
                new com.baomidou.mybatisplus.mapper.EntityWrapper<InterventionExecution>()
                        .eq("huanzhe_id", huanzheId)
                        .orderBy("execution_date", false)
        );
        // 兼容前端预期的数据结构
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (InterventionExecution e : list) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", e.getId());
            m.put("huanzheId", e.getHuanzheId());
            m.put("date", e.getExecutionDate());
            m.put("source", e.getSource());
            Map<String, Object> data = new HashMap<>();
            Object planParsed;
            try {
                planParsed = e.getPlanJson() == null ? null : objectMapper.readValue(e.getPlanJson(), Object.class);
            } catch (Exception ex) {
                planParsed = null;
            }
            data.put("plan", planParsed);
            m.put("data", data);
            // 同时返回顶层 plan 以增强兼容性
            m.put("plan", planParsed);
            dataList.add(m);
        }
        result.put("code", 0);
        result.put("msg", "查询成功");
        result.put("data", dataList);
        return result;
    }

    // 工具：灵活解析日期
    private Date parseFlexibleDate(Object dateVal) {
        if (dateVal == null) return new Date();
        if (dateVal instanceof Date) return (Date) dateVal;
        if (dateVal instanceof Number) return new Date(((Number) dateVal).longValue());
        if (dateVal instanceof String) {
            String s = ((String) dateVal).trim();
            // 处理 ISO 格式和常见分隔符
            List<String> patterns = Arrays.asList(
                    "yyyy-MM-dd HH:mm:ss",
                    "yyyy/MM/dd HH:mm:ss",
                    "yyyy-MM-dd",
                    "yyyy/MM/dd"
            );
            for (String p : patterns) {
                try {
                    java.text.SimpleDateFormat f = new java.text.SimpleDateFormat(p);
                    f.setLenient(false);
                    return f.parse(s);
                } catch (Exception ignore) {}
            }
            try { // ISO-8601
                return java.util.Date.from(java.time.Instant.parse(s));
            } catch (Exception ignore) {}
            try {
                return new Date(s);
            } catch (Exception ignore) {}
        }
        return new Date();
    }

    // 更新执行记录
    @IgnoreAuth
    @PutMapping("/intervention/execution/{executionId}")
    public Map<String, Object> updateExecution(@PathVariable("executionId") Long executionId,
                                               @RequestBody Map<String, Object> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            InterventionExecution e = interventionExecutionService.selectById(executionId);
            if (e != null) {
                Object planObj = body.get("plan");
                if (planObj != null) {
                    e.setPlanJson(objectMapper.writeValueAsString(planObj));
                }
                if (body.get("date") != null) {
                    Object dv = body.get("date");
                    if (dv instanceof Date) {
                        e.setExecutionDate((Date) dv);
                    } else if (dv instanceof String) {
                        try {
                            e.setExecutionDate(new Date((String) dv));
                        } catch (Exception ex) {
                            e.setExecutionDate(new Date());
                        }
                    }
                }
                e.setUpdatedAt(new Date());
                interventionExecutionService.updateById(e);
                result.put("code", 0);
                result.put("msg", "更新成功");
            } else {
                result.put("code", 1);
                result.put("msg", "未找到记录");
            }
            return result;
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", "更新失败：" + e.getMessage());
            return result;
        }
    }

    // 删除执行记录
    @IgnoreAuth
    @DeleteMapping("/intervention/execution/{executionId}")
    public Map<String, Object> deleteExecution(@PathVariable("executionId") Long executionId) {
        Map<String, Object> result = new HashMap<>();
        boolean removed = interventionExecutionService.deleteById(executionId);
        if (removed) {
            result.put("code", 0);
            result.put("msg", "删除成功");
        } else {
            result.put("code", 1);
            result.put("msg", "未找到记录");
        }
        return result;
    }

    // 获取简单统计
    @IgnoreAuth
    @GetMapping("/intervention/statistics/{huanzheId}")
    public Map<String, Object> statistics(@PathVariable("huanzheId") Long huanzheId) {
        Map<String, Object> result = new HashMap<>();
        List<InterventionExecution> list = interventionExecutionService.selectList(
                new com.baomidou.mybatisplus.mapper.EntityWrapper<InterventionExecution>()
                        .eq("huanzhe_id", huanzheId)
        );
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRecords", list.size());
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, -7);
        Date sevenDaysAgo = cal.getTime();
        cal.setTime(now);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date monthStart = cal.getTime();

        int last7 = 0, monthCnt = 0;
        for (InterventionExecution rec : list) {
            Date d = rec.getExecutionDate();
            if (d != null) {
                if (d.after(sevenDaysAgo)) last7++;
                if (d.after(monthStart)) monthCnt++;
            }
        }
        stats.put("last7Days", last7);
        stats.put("thisMonth", monthCnt);

        result.put("code", 0);
        result.put("msg", "统计成功");
        result.put("data", stats);
        return result;
    }
}
    
