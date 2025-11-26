package com.cl.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import com.cl.dto.AskDTO;
import java.util.HashMap;
import java.util.Map;
import com.cl.service.DeepSeekAIService;
import com.cl.service.HealthService;
import com.cl.entity.HealthRecord;

@RestController
@RequestMapping("/healthAdvisory")
public class HealthAdvisoryController {

    @Autowired
    private DeepSeekAIService deepSeekAIService;

    @Autowired
    private HealthService healthService;


    @PostMapping(value = "/ask", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> ask(@RequestBody AskDTO dto) {
        Map<String, Object> res = new HashMap<>();
        try {
            String question = (dto != null) ? dto.getQuestion() : null;
            Long huanzheId = (dto != null) ? dto.getHuanzheId() : null;

            if (question == null || question.trim().isEmpty()) {
                res.put("code", 1);
                res.put("msg", "咨询失败：question 不能为空");
                return res;
            }
            HealthRecord latest = null;
            if (huanzheId != null) {
                latest = healthService.getLatestByHuanzheId(huanzheId);
            }
            Map<String, Object> data = deepSeekAIService.askHealthQuestion(question, latest);
            res.put("code", 0);
            res.put("msg", "ok");
            res.put("data", data);
        } catch (Exception e) {
            res.put("code", 1);
            res.put("msg", "咨询失败：" + e.getMessage());
        }
        return res;
    }
}
