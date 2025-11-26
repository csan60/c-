package com.cl.service.impl;

import com.cl.entity.HealthRecord;
import com.cl.service.DeepSeekAIService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.util.*;

@Service
public class DeepSeekAIServiceImpl implements DeepSeekAIService {

    private static final Logger log = LoggerFactory.getLogger(DeepSeekAIServiceImpl.class);

    @Value("${deepseek.api.key:your-api-key-here}")
    private String apiKey;

    @Value("${deepseek.api.url:https://api.deepseek.com/v1/chat/completions}")
    private String apiUrl;

    // 新增：配置模型参数
    @Value("${deepseek.model:deepseek-r1}")
    private String model;

    // 使用Spring管理的RestTemplate
    @Autowired
    private RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();



    @Override
    public Map<String, Object> generateHealthAssessment(HealthRecord healthRecord) {
        Map<String, Object> assessment = new HashMap<>();

        try {
            String prompt = buildAssessmentPrompt(healthRecord);
            String aiResponse = callDeepSeekAPI(prompt);

            // 解析AI响应的JSON字符串
            Map<String, Object> parsedAnalysis = parseAIResponse(aiResponse);

            // 构建评估结果
            assessment.put("overallScore", calculateOverallScore(healthRecord));
            assessment.put("riskLevel", assessRiskLevel(healthRecord));
            assessment.put("detailedAnalysis", parsedAnalysis);

            // 修复：直接使用AI返回的个性化建议，而不是重新解析
            List<String> aiRecommendations = (List<String>) parsedAnalysis.get("recommendations");
            if (aiRecommendations != null && !aiRecommendations.isEmpty()) {
                assessment.put("recommendations", aiRecommendations);
            } else {
                // 只有在AI没有返回建议时才使用备用方案
                assessment.put("recommendations", extractRecommendations(aiResponse));
            }

            assessment.put("assessmentDate", new Date());

        } catch (Exception e) {
            log.error("生成健康评估时发生异常", e);
            assessment.put("error", "评估生成失败，请稍后重试");
        }

        return assessment;
    }

    @Override
    public Map<String, Object> generateInterventionPlan(HealthRecord healthRecord, Map<String, Object> assessment) {
        Map<String, Object> plan = new HashMap<>();

        try {
            String prompt = buildInterventionPrompt(healthRecord, assessment);
            String aiResponse = callDeepSeekAPI(prompt);

            // 解析AI响应的JSON
            Map<String, Object> parsedPlan = parseInterventionResponse(aiResponse);

            // 构建完整的干预方案数据结构
            plan.put("planTitle", "个性化健康干预方案,符合老人实际行动");
            plan.put("duration", "4周");
            plan.put("priority", assessPriority(assessment));
            plan.put("objectives", extractObjectives(parsedPlan));
            plan.put("measures", buildMeasures(parsedPlan));
            plan.put("timeline", buildTimeline(parsedPlan));
            plan.put("expectedResults", extractExpectedResults(parsedPlan));
            plan.put("precautions", extractPrecautions(parsedPlan));
            plan.put("shortTermGoals", parsedPlan.get("shortTermGoals"));
            plan.put("longTermGoals", parsedPlan.get("longTermGoals"));
            plan.put("dailyPlan", parsedPlan.get("dailyPlan"));
            plan.put("weeklyPlan", parsedPlan.get("weeklyPlan"));
            plan.put("monitoringItems", parsedPlan.get("monitoringItems"));
            plan.put("planDate", new Date());
            plan.put("fullPlan", aiResponse);

        } catch (Exception e) {
            log.error("生成干预方案时发生异常", e);
            plan.put("error", "干预方案生成失败，请稍后重试");
        }

        return plan;
    }

    // 新增：解析干预方案响应
    private Map<String, Object> parseInterventionResponse(String aiResponse) {
        try {
            log.info("AI干预方案原始响应: {}", aiResponse);

            String cleanResponse = cleanAIResponse(aiResponse);
            log.info("清理后的干预方案响应: {}", cleanResponse);

            if (!isValidJsonStart(cleanResponse)) {
                log.warn("AI干预方案响应不是有效JSON格式，使用默认数据");
                return getDefaultInterventionPlan();
            }

            return objectMapper.readValue(cleanResponse, new TypeReference<Map<String, Object>>() {
            });

        } catch (Exception e) {
            log.error("解析AI干预方案响应JSON失败: {}", aiResponse, e);
            return getDefaultInterventionPlan();
        }
    }

    // 新增：获取默认干预方案
    private Map<String, Object> getDefaultInterventionPlan() {
        Map<String, Object> defaultPlan = new HashMap<>();
        defaultPlan.put("shortTermGoals", Arrays.asList("每日步行8000步", "规律作息", "均衡饮食"));
        defaultPlan.put("longTermGoals", Arrays.asList("维持健康BMI", "改善心血管健康", "建立健康生活习惯"));
        defaultPlan.put("dailyPlan", "早晨适量运动30分钟，三餐规律营养均衡，晚上保证充足睡眠");
        defaultPlan.put("weeklyPlan", "每周运动5次，定期健康检查，保持良好心态");
        defaultPlan.put("dietPlan", "低盐低脂饮食，多吃蔬菜水果，控制糖分摄入");
        defaultPlan.put("exercisePlan", "有氧运动为主，每次30-45分钟，强度适中");
        defaultPlan.put("monitoringItems", Arrays.asList("血压", "心率", "体重", "血糖"));
        return defaultPlan;
    }

    // 新增：评估优先级
    private String assessPriority(Map<String, Object> assessment) {
        if (assessment == null) return "中优先级";

        String riskLevel = (String) assessment.get("riskLevel");
        if ("高".equals(riskLevel)) {
            return "高优先级";
        } else if ("低".equals(riskLevel)) {
            return "低优先级";
        }
        return "中优先级";
    }

    // 新增：提取目标
    private String extractObjectives(Map<String, Object> parsedPlan) {
        List<String> shortTermGoals = (List<String>) parsedPlan.get("shortTermGoals");
        List<String> longTermGoals = (List<String>) parsedPlan.get("longTermGoals");

        StringBuilder objectives = new StringBuilder();
        if (shortTermGoals != null && !shortTermGoals.isEmpty()) {
            objectives.append("短期目标：").append(String.join("、", shortTermGoals)).append("；");
        }
        if (longTermGoals != null && !longTermGoals.isEmpty()) {
            objectives.append("长期目标：").append(String.join("、", longTermGoals));
        }

        return objectives.length() > 0 ? objectives.toString() : "改善老人整体健康状况，建立良好生活习惯";
    }

    // 新增：构建干预措施
    private List<Map<String, Object>> buildMeasures(Map<String, Object> parsedPlan) {
        List<Map<String, Object>> measures = new ArrayList<>();

        // 饮食干预
        String dietPlan = (String) parsedPlan.get("dietPlan");
        if (dietPlan != null && !dietPlan.isEmpty()) {
            Map<String, Object> dietMeasure = new HashMap<>();
            dietMeasure.put("category", "饮食干预");
            dietMeasure.put("importance", "重要");
            dietMeasure.put("description", dietPlan);
            dietMeasure.put("actions", Arrays.asList("制定饮食计划", "控制热量摄入", "增加蔬果比例"));
            measures.add(dietMeasure);
        }

        // 运动干预
        String exercisePlan = (String) parsedPlan.get("exercisePlan");
        if (exercisePlan != null && !exercisePlan.isEmpty()) {
            Map<String, Object> exerciseMeasure = new HashMap<>();
            exerciseMeasure.put("category", "运动干预");
            exerciseMeasure.put("importance", "重要");
            exerciseMeasure.put("description", exercisePlan);
            exerciseMeasure.put("actions", Arrays.asList("制定运动计划", "循序渐进增加强度", "记录运动数据"));
            measures.add(exerciseMeasure);
        }

        // 监测干预
        List<String> monitoringItems = (List<String>) parsedPlan.get("monitoringItems");
        if (monitoringItems != null && !monitoringItems.isEmpty()) {
            Map<String, Object> monitorMeasure = new HashMap<>();
            monitorMeasure.put("category", "健康监测");
            monitorMeasure.put("importance", "一般");
            monitorMeasure.put("description", "定期监测关键健康指标");
            monitorMeasure.put("actions", monitoringItems);
            measures.add(monitorMeasure);
        }

        return measures;
    }

    // 新增：构建时间线
    private List<Map<String, Object>> buildTimeline(Map<String, Object> parsedPlan) {
        List<Map<String, Object>> timeline = new ArrayList<>();

        // 第一周
        Map<String, Object> week1 = new HashMap<>();
        week1.put("period", "第1周");
        week1.put("tasks", Arrays.asList("建立基础运动习惯", "调整饮食结构", "开始健康监测"));
        timeline.add(week1);

        // 第二周
        Map<String, Object> week2 = new HashMap<>();
        week2.put("period", "第2周");
        week2.put("tasks", Arrays.asList("增加运动强度", "优化饮食搭配", "记录健康数据"));
        timeline.add(week2);

        // 第三周
        Map<String, Object> week3 = new HashMap<>();
        week3.put("period", "第3周");
        week3.put("tasks", Arrays.asList("巩固健康习惯", "评估阶段效果", "调整干预方案"));
        timeline.add(week3);

        // 第四周
        Map<String, Object> week4 = new HashMap<>();
        week4.put("period", "第4周");
        week4.put("tasks", Arrays.asList("完善生活方式", "制定长期计划", "总结干预效果"));
        timeline.add(week4);

        return timeline;
    }

    // 新增：提取预期效果
    private String extractExpectedResults(Map<String, Object> parsedPlan) {
        return "通过4周的系统干预，预期改善健康指标，建立良好生活习惯，提升老人整体健康水平";
    }

    // 新增：提取注意事项
    private List<String> extractPrecautions(Map<String, Object> parsedPlan) {
        return Arrays.asList(
                "运动前请做好热身准备",
                "如有不适请及时就医",
                "坚持记录健康数据",
                "保持积极心态",
                "定期复查健康指标"
        );
    }

    private String callDeepSeekAPI(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> requestBody = new HashMap<>();
            // 修改：使用配置的模型而不是硬编码
            requestBody.put("model", model);

            List<Map<String, String>> messages = new ArrayList<>();

            // 修改系统消息，针对R1模型优化
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", "你是专业医学健康评估专家，具备深度推理能力。重要：你必须严格按照JSON格式返回结果，不要添加任何解释文字、标题或代码块标记。直接输出有效的JSON对象，以{开头，以}结尾。利用你的推理能力提供更准确的健康评估。");
            messages.add(systemMessage);

            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);
            messages.add(userMessage);

            requestBody.put("messages", messages);
            requestBody.put("max_tokens", 1500); // R1模型可以处理更多token
            requestBody.put("temperature", 0.2); // R1模型推理能力强，可以降低温度

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                return jsonNode.path("choices").get(0).path("message").path("content").asText();
            } else {
                log.error("DeepSeek API调用失败，状态码：{}", response.getStatusCode());
                return "AI服务暂时不可用，请稍后再试。";
            }

        } catch (Exception e) {
            log.error("调用DeepSeek API时发生异常", e);
            return "AI服务调用失败，请稍后再试。";
        }
    }

    private String buildAssessmentPrompt(HealthRecord healthRecord) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是临床健康评估系统的专家AI，具备深度推理和分析能力。请运用你的推理能力，深入分析以下健康数据的关联性和潜在风险。\n\n");

        // 添加健康数据
        addHealthDataToPrompt(prompt, healthRecord);

        prompt.append("\n\n评估要求：\n");
        prompt.append("1. 分析各项指标之间的相关性和因果关系\n");
        prompt.append("2. 推理可能的健康风险发展趋势\n");
        prompt.append("3. 基于医学知识进行逻辑推导\n");
        prompt.append("4. 提供有理有据的个性化建议\n");
        prompt.append("5. 请你为老人提供的建议便于理解\n");
        prompt.append("6. 必须直接返回JSON格式，展现你的推理过程\n\n");

        // 新增：具体的评估指导
        prompt.append("特别评估指导：\n");
        prompt.append("- nutritionStatus: 根据BMI、体重变化趋势推断营养状况（营养不良/正常/营养过剩）\n");
        prompt.append("- sleepQuality: 根据心率、血压、整体健康状况推断睡眠质量（差/一般/良好）\n");
        prompt.append("- stressLevel: 根据血压、心率、运动量综合评估压力水平（低/适中/偏高）\n\n");

        prompt.append("返回格式示例：\n");
        prompt.append("{\n");
        prompt.append("  \"overallScore\": 75,\n");
        prompt.append("  \"riskLevel\": \"中\",\n");
        prompt.append("  \"healthTrend\": \"稳定\",\n");
        prompt.append("  \"potentialRisks\": \"血压偏高\",\n");
        prompt.append("  \"improvementSuggestion\": \"增加运动\",\n");
        prompt.append("  \"nutritionStatus\": \"正常\",\n");
        prompt.append("  \"sleepQuality\": \"良好\",\n");
        prompt.append("  \"stressLevel\": \"适中\",\n");
        prompt.append("  \"recommendations\": [\"建议1\", \"建议2\", \"建议3\"]\n");
        prompt.append("}\n\n");

        prompt.append("请根据实际健康数据填写评估内容，直接输出JSON，不要任何其他文字。");

        return prompt.toString();
    }

    // 新增：解析AI响应的JSON字符串
    // 修改parseAIResponse方法
    private Map<String, Object> parseAIResponse(String aiResponse) {
        try {
            log.info("AI原始响应: {}", aiResponse);

            // 清理AI响应字符串
            String cleanResponse = cleanAIResponse(aiResponse);
            log.info("清理后的响应: {}", cleanResponse);

            // 验证是否为有效JSON
            if (!isValidJsonStart(cleanResponse)) {
                log.warn("AI响应不是有效JSON格式，使用默认数据");
                return getDefaultAnalysis();
            }

            // 解析JSON
            return objectMapper.readValue(cleanResponse, new TypeReference<Map<String, Object>>() {
            });

        } catch (Exception e) {
            log.error("解析AI响应JSON失败: {}", aiResponse, e);
            return getDefaultAnalysis();
        }
    }

    // 新增：检查是否为有效JSON开头
    private boolean isValidJsonStart(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        String trimmed = str.trim();
        return trimmed.startsWith("{") && trimmed.endsWith("}");
    }

    // 修改cleanAIResponse方法
    private String cleanAIResponse(String response) {
        if (response == null || response.trim().isEmpty()) {
            return "{}";
        }

        String cleaned = response.trim();

        // 移除可能的代码块标记
        cleaned = cleaned.replaceAll("^```json\\s*", "");
        cleaned = cleaned.replaceAll("^```\\s*", "");
        cleaned = cleaned.replaceAll("```\\s*$", "");

        // 移除非JSON前缀文字（如"AI医务信息分析"等）
        if (cleaned.contains("{")) {
            int jsonStart = cleaned.indexOf("{");
            cleaned = cleaned.substring(jsonStart);
        }

        // 移除非JSON后缀文字
        if (cleaned.contains("}")) {
            int jsonEnd = cleaned.lastIndexOf("}");
            cleaned = cleaned.substring(0, jsonEnd + 1);
        }

        // 如果仍然不是有效JSON，返回空对象
        if (!cleaned.startsWith("{") || !cleaned.endsWith("}")) {
            return "{}";
        }

        return cleaned;
    }

    // 新增：获取默认分析数据
    private Map<String, Object> getDefaultAnalysis() {
        Map<String, Object> defaultAnalysis = new HashMap<>();
        defaultAnalysis.put("overallScore", 70);
        defaultAnalysis.put("riskLevel", "中");
        defaultAnalysis.put("healthTrend", "稳定");
        defaultAnalysis.put("potentialRisks", "AI分析异常，建议重新评估");
        defaultAnalysis.put("improvementSuggestion", "保持健康生活方式");
        defaultAnalysis.put("nutritionStatus", "待评估");
        defaultAnalysis.put("sleepQuality", "待评估");
        defaultAnalysis.put("stressLevel", "待评估");
        defaultAnalysis.put("recommendations", Arrays.asList("定期体检", "均衡饮食", "适量运动"));
        return defaultAnalysis;
    }

    private String buildInterventionPrompt(HealthRecord healthRecord, Map<String, Object> assessment) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一名资深健康干预专家，请基于以下老头的健康数据和系统评估结果，制定一份可操作性强的干预方案要根据老年人的身体情况生成方案：\n\n");

        // 添加健康数据
        addHealthDataToPrompt(prompt, healthRecord);

        // 添加评估结果
        if (assessment != null && !assessment.isEmpty()) {
            prompt.append("\n评估结果如下：\n");
            prompt.append("风险等级：").append(assessment.get("riskLevel")).append("\n");
            prompt.append("总体评分：").append(assessment.get("overallScore")).append("\n");
        }

        prompt.append("\n请使用如下JSON格式输出干预方案：\n");
        prompt.append("{\n");
        prompt.append("  \"shortTermGoals\": [\"1-2周内应达成的目标，如：每日至少步行7000步\"],\n");
        prompt.append("  \"longTermGoals\": [\"1-3个月内达成的目标，如：BMI降至正常范围\"],\n");
        prompt.append("  \"dailyPlan\": \"每天的推荐生活作息和饮食建议\",\n");
        prompt.append("  \"weeklyPlan\": \"每周计划内容，如运动频率、饮食安排、休息模式\",\n");
        prompt.append("  \"dietPlan\": \"专业饮食建议，例如限制脂肪摄入、增加膳食纤维\",\n");
        prompt.append("  \"exercisePlan\": \"个性化运动处方，如快走30分钟/天\",\n");
        prompt.append("  \"monitoringItems\": [\"需监测指标，如血压、血糖、心率\"]\n");
        prompt.append("}\n");
        prompt.append("输出语言要求简明、专业、切实可行，仅输出JSON内容。");

        return prompt.toString();
    }

    private void addHealthDataToPrompt(StringBuilder prompt, HealthRecord healthRecord) {
        if (healthRecord.getHeightCm() != null && healthRecord.getWeightKg() != null) {
            prompt.append("身高：").append(healthRecord.getHeightCm()).append("cm\n");
            prompt.append("体重：").append(healthRecord.getWeightKg()).append("kg\n");
        }

        if (healthRecord.getBmi() != null) {
            prompt.append("BMI：").append(healthRecord.getBmi()).append("\n");
        }

        if (healthRecord.getSystolic() != null && healthRecord.getDiastolic() != null) {
            prompt.append("血压：").append(healthRecord.getSystolic())
                    .append("/").append(healthRecord.getDiastolic()).append("mmHg\n");
        }

        if (healthRecord.getHeartRate() != null) {
            prompt.append("心率：").append(healthRecord.getHeartRate()).append("次/分钟\n");
        }

        if (healthRecord.getStepsToday() != null) {
            prompt.append("今日步数：").append(healthRecord.getStepsToday()).append("步\n");
        }
    }

    private int calculateOverallScore(HealthRecord healthRecord) {
        int score = 100;

        // BMI评分
        if (healthRecord.getBmi() != null) {
            double bmi = healthRecord.getBmi().doubleValue();
            if (bmi < 18.5 || bmi > 28) {
                score -= 20;
            } else if (bmi < 20 || bmi > 25) {
                score -= 10;
            }
        }

        // 血压评分
        if (healthRecord.getSystolic() != null && healthRecord.getDiastolic() != null) {
            if (healthRecord.getSystolic() > 140 || healthRecord.getDiastolic() > 90) {
                score -= 25;
            } else if (healthRecord.getSystolic() > 130 || healthRecord.getDiastolic() > 85) {
                score -= 15;
            }
        }

        // 心率评分
        if (healthRecord.getHeartRate() != null) {
            if (healthRecord.getHeartRate() > 100 || healthRecord.getHeartRate() < 60) {
                score -= 15;
            }
        }

        // 运动评分
        if (healthRecord.getStepsToday() != null) {
            if (healthRecord.getStepsToday() < 5000) {
                score -= 20;
            } else if (healthRecord.getStepsToday() < 8000) {
                score -= 10;
            }
        }

        return Math.max(0, score);
    }

    private String assessRiskLevel(HealthRecord healthRecord) {
        int riskPoints = 0;

        // BMI风险
        if (healthRecord.getBmi() != null) {
            double bmi = healthRecord.getBmi().doubleValue();
            if (bmi < 18.5 || bmi > 30) {
                riskPoints += 3;
            } else if (bmi > 25) {
                riskPoints += 1;
            }
        }

        // 血压风险
        if (healthRecord.getSystolic() != null && healthRecord.getDiastolic() != null) {
            if (healthRecord.getSystolic() > 140 || healthRecord.getDiastolic() > 90) {
                riskPoints += 3;
            } else if (healthRecord.getSystolic() > 130 || healthRecord.getDiastolic() > 85) {
                riskPoints += 1;
            }
        }

        // 心率风险
        if (healthRecord.getHeartRate() != null) {
            if (healthRecord.getHeartRate() > 100 || healthRecord.getHeartRate() < 50) {
                riskPoints += 2;
            }
        }

        if (riskPoints >= 5) {
            return "高";
        } else if (riskPoints >= 2) {
            return "中";
        } else {
            return "低";
        }
    }

    private List<String> extractRecommendations(String aiResponse) {
        List<String> recommendations = new ArrayList<>();
        // 简单的文本解析，实际项目中可以使用更复杂的NLP处理
        String[] lines = aiResponse.split("\n");
        for (String line : lines) {
            if (line.contains("建议") || line.contains("推荐")) {
                recommendations.add(line.trim());
            }
        }
        if (recommendations.isEmpty()) {
            recommendations.add("保持健康的生活方式");
            recommendations.add("定期进行健康检查");
            recommendations.add("适量运动，均衡饮食");
        }
        return recommendations;
    }

     @Override
    public Map<String, Object> askHealthQuestion(String question, HealthRecord healthRecord) {
        Map<String, Object> result = new HashMap<>();
        try {
            StringBuilder prompt = new StringBuilder();
            prompt.append("你是一名面向老年人的健康科普与生活指导专家。请用通俗、温和、步骤清晰的语言回答问题，避免夸大或直接给出医疗诊断，必要时提醒就医。\n");
            prompt.append("回答要求：\n");
            prompt.append("1) 先简要判断问题的关注点；2) 给出日常可执行的小步骤建议；3) 给出何时需要就医的提示；4) 全部使用中文；5) 尽量不超过200字；\n\n");
            if (healthRecord != null) {
                prompt.append("（以下为该老人的最近一次健康信息，供你理解背景并给出更贴合老人的建议）\n");
                addHealthDataToPrompt(prompt, healthRecord);
                prompt.append("\n");
            }
            prompt.append("老人提问：").append(question).append("\n");
            prompt.append("请直接用自然语言作答，不要返回JSON或代码块。");

            String aiResponse = callDeepSeekAPI(prompt.toString());
            String answer = cleanPlainText(aiResponse);

            result.put("answer", answer);
            result.put("disclaimer", "温馨提示：本回答仅作健康科普，不能替代医生诊疗。如出现胸痛、呼吸困难、意识障碍等紧急情况，请立即就医或拨打120。");
            result.put("suggestedQuestions", Arrays.asList(
                    "我最近的血压需要注意什么？",
                    "适合我的日常运动建议是什么？",
                    "饮食上有哪些需要注意的地方？"
            ));
            return result;
        } catch (Exception e) {
            log.error("AI问答失败", e);
            result.put("answer", "抱歉，我现在无法回答，请稍后再试。");
            result.put("disclaimer", "温馨提示：如有不适，请及时就医。");
            result.put("suggestedQuestions", Collections.emptyList());
            return result;
        }
    }

    // ... existing code ...

    // 轻度清理AI返回的普通文本（移除可能的代码块标记和多余空白）
    private String cleanPlainText(String s) {
        if (s == null) return "抱歉，我现在无法回答，请稍后再试。";
        String t = s.trim();
        t = t.replaceAll("^```json\\s*", "");
        t = t.replaceAll("^```\\s*", "");
        t = t.replaceAll("```\\s*$", "");
        return t.trim();
    }


}