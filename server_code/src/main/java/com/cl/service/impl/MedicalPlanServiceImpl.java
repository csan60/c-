package com.cl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.dao.MedicalPlanDao;
import com.cl.entity.MedicalPlan;
import com.cl.service.MedicalPlanService;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class MedicalPlanServiceImpl extends ServiceImpl<MedicalPlanDao, MedicalPlan> implements MedicalPlanService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<MedicalPlan> page = this.selectPage(
                new Query<MedicalPlan>(params).getPage(),
                new EntityWrapper<MedicalPlan>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<MedicalPlan> wrapper) {
        Page<MedicalPlan> page = new Query<MedicalPlan>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        PageUtils pageUtil = new PageUtils(page);
        return pageUtil;
    }

    @Override
    public List<MedicalPlan> selectListView(Wrapper<MedicalPlan> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public MedicalPlan selectView(Wrapper<MedicalPlan> wrapper) {
        return baseMapper.selectView(wrapper);
    }

    @Override
    public List<MedicalPlan> createMedicalPlansFromOcr(Long olderUserId, String ocrResult) {
        List<Map<String, String>> parsedData = parseOcrText(ocrResult);
        List<MedicalPlan> plans = new ArrayList<>();
        
        for (Map<String, String> drugInfo : parsedData) {
            MedicalPlan plan = new MedicalPlan();
            plan.setOlderUserId(olderUserId);
            plan.setDrugName(drugInfo.get("drugName"));
            plan.setDosage(drugInfo.get("dosage"));
            plan.setUsage(drugInfo.get("usage"));
            plan.setFrequency(drugInfo.get("frequency"));
            plan.setRemarks(drugInfo.get("remarks"));
            plan.setPlanStatus(1); // 默认有效
            plan.setCreatedAt(LocalDateTime.now());
            plan.setUpdatedAt(LocalDateTime.now());
            
            // 解析开始日期
            String startDateStr = drugInfo.get("startDate");
            if (startDateStr != null && !startDateStr.isEmpty()) {
                try {
                    plan.setStartDate(LocalDate.parse(startDateStr));
                } catch (Exception e) {
                    plan.setStartDate(LocalDate.now()); // 默认今天开始
                }
            } else {
                plan.setStartDate(LocalDate.now());
            }
            
            // 解析结束日期
            String endDateStr = drugInfo.get("endDate");
            if (endDateStr != null && !endDateStr.isEmpty()) {
                try {
                    plan.setEndDate(LocalDate.parse(endDateStr));
                } catch (Exception e) {
                    // 如果没有结束日期，设为null（长期用药）
                    plan.setEndDate(null);
                }
            }
            
            plans.add(plan);
        }
        
        // 批量保存
        for (MedicalPlan plan : plans) {
            this.insert(plan);
        }
        
        log.info("成功创建{}个医嘱计划，用户ID：{}", plans.size(), olderUserId);
        return plans;
    }

    @Override
    public List<MedicalPlan> getActivePlans(Long olderUserId) {
        EntityWrapper<MedicalPlan> wrapper = new EntityWrapper<>();
        wrapper.eq("older_user_id", olderUserId)
               .eq("plan_status", 1)
               .orderBy("created_at", false);
        return this.selectList(wrapper);
    }

    @Override
    public List<Map<String, String>> parseOcrText(String ocrText) {
        List<Map<String, String>> results = new ArrayList<>();
        
        if (ocrText == null || ocrText.trim().isEmpty()) {
            return results;
        }
        
        log.info("开始解析OCR文本：{}", ocrText);
        
        // 按行分割OCR文本
        String[] lines = ocrText.split("\\n|\\r\\n|\\r");
        
        // 增强的药品名称正则表达式
        Pattern drugNamePattern = Pattern.compile(
            "([\\u4e00-\\u9fa5A-Za-z0-9]+(?:片|胶囊|颗粒|丸|散|膏|贴|注射液|口服液|糖浆|滴剂|喷雾剂|软膏|乳膏|栓|泡腾片|缓释片|肠溶片|咀嚼片))");
        
        // 增强的剂量正则表达式
        Pattern dosagePattern = Pattern.compile(
            "(\\d+(?:\\.\\d+)?(?:mg|g|ml|μg|mcg|ug|片|粒|袋|支|贴|滴|喷|次)(?:/(?:次|日|天))?)"
        );
        
        // 增强的频次正则表达式
        Pattern frequencyPattern = Pattern.compile(
            "(?:每日|每天|一日|一天|日)\\s*(\\d+)\\s*(?:次|回)|(?:一日|每日|每天)\\s*(\\d+)\\s*(?:次|回)|(?:bid|tid|qid|qd|q\\d+h)"
        );
        
        // 增强的用法正则表达式
        Pattern usagePattern = Pattern.compile(
            "(口服|外用|注射|肌注|静注|静滴|滴眼|含服|嚼服|吞服|舌下含服|饭前|饭后|空腹|餐前|餐后|睡前)"
        );
        
        // 时间相关正则表达式
        Pattern timePattern = Pattern.compile(
            "(\\d{4}[-/年]\\d{1,2}[-/月]\\d{1,2}[日]?|\\d{1,2}[-/月]\\d{1,2}[日]?)"
        );
        
        Map<String, String> currentDrug = new HashMap<>();
        StringBuilder allText = new StringBuilder();
        
        // 合并所有文本用于全局匹配
        for (String line : lines) {
            allText.append(line).append(" ");
        }
        String fullText = allText.toString();
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            log.debug("处理行：{}", line);
            
            // 查找药品名称
            Matcher drugMatcher = drugNamePattern.matcher(line);
            if (drugMatcher.find()) {
                // 如果找到新的药品名称，保存之前的药品信息
                if (!currentDrug.isEmpty() && currentDrug.containsKey("drugName")) {
                    results.add(new HashMap<>(currentDrug));
                    currentDrug.clear();
                }
                currentDrug.put("drugName", drugMatcher.group(1));
                log.debug("识别到药品：{}", drugMatcher.group(1));
            }
            
            // 查找剂量
            Matcher dosageMatcher = dosagePattern.matcher(line);
            if (dosageMatcher.find()) {
                currentDrug.put("dosage", dosageMatcher.group(1));
                log.debug("识别到剂量：{}", dosageMatcher.group(1));
            }
            
            // 查找频次
            Matcher frequencyMatcher = frequencyPattern.matcher(line);
            if (frequencyMatcher.find()) {
                currentDrug.put("frequency", frequencyMatcher.group(0));
                log.debug("识别到频次：{}", frequencyMatcher.group(0));
            }
            
            // 查找用法
            Matcher usageMatcher = usagePattern.matcher(line);
            if (usageMatcher.find()) {
                String usage = currentDrug.get("usage");
                if (usage == null) {
                    currentDrug.put("usage", usageMatcher.group(1));
                } else {
                    currentDrug.put("usage", usage + "," + usageMatcher.group(1));
                }
                log.debug("识别到用法：{}", usageMatcher.group(1));
            }
            
            // 查找时间信息
            Matcher timeMatcher = timePattern.matcher(line);
            if (timeMatcher.find()) {
                String timeStr = timeMatcher.group(1);
                if (!currentDrug.containsKey("startDate")) {
                    currentDrug.put("startDate", normalizeDate(timeStr));
                    log.debug("识别到开始时间：{}", timeStr);
                }
            }
        }
        
        // 添加最后一个药品
        if (!currentDrug.isEmpty() && currentDrug.containsKey("drugName")) {
            results.add(currentDrug);
        }
        
        // 为没有完整信息的药品设置默认值
        for (Map<String, String> drug : results) {
            if (!drug.containsKey("dosage") || drug.get("dosage").isEmpty()) {
                drug.put("dosage", "按医嘱");
            }
            if (!drug.containsKey("frequency") || drug.get("frequency").isEmpty()) {
                drug.put("frequency", "按医嘱");
            }
            if (!drug.containsKey("usage") || drug.get("usage").isEmpty()) {
                drug.put("usage", "口服");
            }
            if (!drug.containsKey("remarks")) {
                drug.put("remarks", "OCR自动识别");
            }
        }
        
        log.info("OCR文本解析完成，识别到{}个药品", results.size());
        return results;
    }
    
    /**
     * 标准化日期格式
     */
    private String normalizeDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return LocalDate.now().toString();
        }
        
        try {
            // 处理中文日期格式
            dateStr = dateStr.replace("年", "-")
                            .replace("月", "-")
                            .replace("日", "")
                            .replace("/", "-");
            
            // 尝试解析不同格式的日期
            if (dateStr.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
                LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-M-d"));
                return date.toString();
            } else if (dateStr.matches("\\d{1,2}-\\d{1,2}")) {
                // 假设是当年的日期
                String currentYear = String.valueOf(LocalDate.now().getYear());
                LocalDate date = LocalDate.parse(currentYear + "-" + dateStr, DateTimeFormatter.ofPattern("yyyy-M-d"));
                return date.toString();
            }
        } catch (Exception e) {
            log.warn("日期解析失败：{}", dateStr, e);
        }
        
        return LocalDate.now().toString();
    }
}