package com.cl.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.cl.entity.HealthRecord;
import com.cl.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/health")
@CrossOrigin
public class HealthController {

    @Autowired
    private HealthService healthService;

    /**
     * 保存或更新健康记录
     */
    @PostMapping("/saveOrUpdate")
    public Map<String, Object> saveOrUpdate(@RequestBody HealthRecord healthRecord) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = healthService.saveOrUpdateWithCalculation(healthRecord);
            if (success) {
                result.put("code", 0);
                result.put("msg", "保存成功");
                result.put("data", healthRecord);

                // 检查健康异常
                Map<String, Object> checkResult = healthService.checkHealthAbnormal(healthRecord);
                result.put("healthCheck", checkResult);
            } else {
                result.put("code", 1);
                result.put("msg", "保存失败");
            }
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", "保存失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 根据ID查询健康记录
     */
    @GetMapping("/info/{id}")
    public Map<String, Object> info(@PathVariable("id") Long id) {
        Map<String, Object> result = new HashMap<>();
        HealthRecord healthRecord = healthService.selectById(id);  // 旧版本使用selectById
        if (healthRecord != null) {
            result.put("code", 0);
            result.put("data", healthRecord);
        } else {
            result.put("code", 1);
            result.put("msg", "记录不存在");
        }
        return result;
    }

    /**
     * 获取患者最新健康记录
     */
    @GetMapping("/latest")
    public Map<String, Object> getLatest(@RequestParam("huanzheId") Long huanzheId) {
        Map<String, Object> result = new HashMap<>();
        HealthRecord healthRecord = healthService.getLatestByHuanzheId(huanzheId);
        if (healthRecord != null) {
            result.put("code", 0);
            result.put("data", healthRecord);
        } else {
            result.put("code", 1);
            result.put("msg", "暂无健康记录");
        }
        return result;
    }


    /**
     * 根据时间范围查询健康记录
     */
    /**
     * 根据时间范围查询健康记录
     * 如果不传日期参数，则查询全部记录
     */
    @GetMapping("/history")
    public Map<String, Object> getHistory(
            @RequestParam("huanzheId") Long huanzheId,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        Map<String, Object> result = new HashMap<>();

        // 如果没有传递日期参数，设置默认值查询全部记录
        if (startDate == null && endDate == null) {
            // 查询全部记录：设置一个很早的开始日期和当前日期作为结束日期
            Calendar cal = Calendar.getInstance();
            cal.set(2000, 0, 1); // 设置为2000年1月1日
            startDate = cal.getTime();
            endDate = new Date(); // 当前日期
        } else if (startDate == null) {
            // 只传了结束日期，开始日期设为很早的日期
            Calendar cal = Calendar.getInstance();
            cal.set(2000, 0, 1);
            startDate = cal.getTime();
        } else if (endDate == null) {
            // 只传了开始日期，结束日期设为当前日期
            endDate = new Date();
        }

        List<HealthRecord> records = healthService.getByHuanzheIdAndDateRange(huanzheId, startDate, endDate);
        result.put("code", 0);
        result.put("data", records);
        return result;
    }

    /**
     * 获取健康数据统计
     */
    @GetMapping("/stats")
    public Map<String, Object> getStats(@RequestParam("huanzheId") Long huanzheId) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> stats = healthService.getHealthStats(huanzheId);
        result.put("code", 0);
        result.put("data", stats);
        return result;
    }

    /**
     * 获取健康趋势数据
     */
    @GetMapping("/trend")
    public Map<String, Object> getTrend(@RequestParam("huanzheId") Long huanzheId) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> trend = healthService.getHealthTrend(huanzheId);
        result.put("code", 0);
        result.put("data", trend);
        return result;
    }

    /**
     * 删除健康记录
     */
    @PostMapping("/delete")
    public Map<String, Object> delete(@RequestBody Long[] ids) {
        Map<String, Object> result = new HashMap<>();
        try {
            for (Long id : ids) {
                healthService.deleteById(id);  // 旧版本使用deleteById
            }
            result.put("code", 0);
            result.put("msg", "删除成功");
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", "删除失败：" + e.getMessage());
        }
        return result;
    }


    /**
     * 可视化图表数据接口
     * 场景：前端根据患者ID与时间范围获取折线图数据
     * 入参：huanzheId（必填），rangeDays（可选，默认30），metrics（可选，逗号分隔，如：weight,heartRate,systolic,diastolic,steps,calories）
     * 预期：返回每个指标的时间序列数据，以及对应的最小/最大范围，便于前端绘图自适配
     */
    @GetMapping("/chartData")
    public Map<String, Object> getChartData(
            @RequestParam("huanzheId") Long huanzheId,
            @RequestParam(value = "rangeDays", required = false) Integer rangeDays,
            @RequestParam(value = "metrics", required = false) String metrics
    ) {
        Map<String, Object> result = new HashMap<>();
        try {
            int days = (rangeDays == null || rangeDays <= 0) ? 30 : rangeDays;
            Calendar cal = Calendar.getInstance();
            Date end = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, -days);
            Date start = cal.getTime();

            List<HealthRecord> records = healthService.getByHuanzheIdAndDateRange(huanzheId, start, end);
            // 按日期升序，确保折线图按时间顺序绘制
            records.sort(Comparator.comparing(HealthRecord::getRecordDate));

            // 默认指标集合
            Set<String> defaultMetrics = new LinkedHashSet<>(Arrays.asList(
                    "weight", "heartRate", "systolic", "diastolic", "steps", "calories",
                    "bloodOxygen", "bloodSugar", "bmi", "temperature", "bodyFat", "bloodLipids"
            ));
            Set<String> metricsSet;
            if (metrics != null && !metrics.trim().isEmpty()) {
                metricsSet = new LinkedHashSet<>();
                for (String m : metrics.split(",")) {
                    String key = m.trim();
                    if (!key.isEmpty()) metricsSet.add(key);
                }
            } else {
                metricsSet = defaultMetrics;
            }

            Map<String, List<Map<String, Object>>> series = new LinkedHashMap<>();
            Map<String, Map<String, Object>> ranges = new LinkedHashMap<>();
            List<String> labels = new ArrayList<>();

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            for (HealthRecord r : records) {
                String dateStr = sdf.format(r.getRecordDate());
                labels.add(dateStr);

                // 工具：加入点并维护范围
            // 工具：加入点并维护范围（含异常值过滤）
            java.util.function.BiConsumer<String, Number> addPoint = (key, val) -> {
                    if (val == null) return;
                    double v = val.doubleValue();
                    // 过滤明显异常值与测试数据
                    if (!isValidMetricValue(key, v)) return;
                    List<Map<String, Object>> list = series.computeIfAbsent(key, k -> new ArrayList<>());
                    Map<String, Object> point = new HashMap<>();
                    point.put("date", dateStr);
                    point.put("value", val);
                    list.add(point);
                    Map<String, Object> range = ranges.computeIfAbsent(key, k -> {
                        Map<String, Object> m = new HashMap<>();
                        m.put("min", val.doubleValue());
                        m.put("max", val.doubleValue());
                        return m;
                    });
                    range.put("min", Math.min(v, ((Number) range.get("min")).doubleValue()));
                    range.put("max", Math.max(v, ((Number) range.get("max")).doubleValue()));
            };

                if (metricsSet.contains("weight") && r.getWeightKg() != null)
                    addPoint.accept("weight", r.getWeightKg());
                if (metricsSet.contains("heartRate") && r.getHeartRate() != null)
                    addPoint.accept("heartRate", r.getHeartRate());
                if (metricsSet.contains("systolic") && r.getSystolic() != null)
                    addPoint.accept("systolic", r.getSystolic());
                if (metricsSet.contains("diastolic") && r.getDiastolic() != null)
                    addPoint.accept("diastolic", r.getDiastolic());
                if (metricsSet.contains("steps") && r.getStepsToday() != null)
                    addPoint.accept("steps", r.getStepsToday());
                if (metricsSet.contains("calories") && r.getCaloriesBurned() != null)
                    addPoint.accept("calories", r.getCaloriesBurned());
                if (metricsSet.contains("bloodOxygen") && r.getBloodOxygen() != null)
                    addPoint.accept("bloodOxygen", r.getBloodOxygen());
                if (metricsSet.contains("bloodSugar") && r.getBloodSugar() != null)
                    addPoint.accept("bloodSugar", r.getBloodSugar());
                if (metricsSet.contains("bmi") && r.getBmi() != null) addPoint.accept("bmi", r.getBmi());
                if (metricsSet.contains("temperature") && r.getTemperature() != null)
                    addPoint.accept("temperature", r.getTemperature());
                if (metricsSet.contains("bodyFat") && r.getBodyFat() != null)
                    addPoint.accept("bodyFat", r.getBodyFat());
                if (metricsSet.contains("bloodLipids") && r.getBloodLipids() != null)
                    addPoint.accept("bloodLipids", r.getBloodLipids());
            }

            Map<String, Object> data = new HashMap<>();
            data.put("labels", labels);
            data.put("series", series);
            data.put("ranges", ranges);
            data.put("rangeDays", days);

            result.put("code", 0);
            result.put("msg", "查询成功");
            result.put("data", data);
            return result;
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", "查询失败：" + e.getMessage());
            return result;
        }
    }

    /**
     * 指标有效性校验：过滤显著异常值，避免测试数据影响前端图表
     */
    private boolean isValidMetricValue(String key, double v) {
        switch (key) {
            case "heartRate":
                return v >= 30 && v <= 200;
            case "bloodOxygen":
                return v >= 70 && v <= 100;
            case "systolic":
                return v >= 50 && v <= 250;
            case "diastolic":
                return v >= 30 && v <= 150;
            case "bloodSugar":
                return v >= 1 && v <= 30;
            case "weight":
                return v >= 30 && v <= 200;
            case "steps":
                return v >= 0 && v <= 50000;
            case "calories":
                return v >= 0 && v <= 10000;
            case "bmi":
                return v >= 10 && v <= 50;
            case "temperature":
                return v >= 35 && v <= 42;
            case "bodyFat":
                return v >= 3 && v <= 80;
            case "bloodLipids":
                return v >= 1 && v <= 20;
            default:
                // 默认通过
                return true;
        }
    }
}
