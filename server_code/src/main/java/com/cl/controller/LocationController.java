package com.cl.controller;

import com.cl.entity.LocationReport;
import com.cl.service.LocationReportService;
import com.cl.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/location")
@CrossOrigin
public class LocationController {

    @Autowired
    private LocationReportService locationReportService;

    @PostMapping("/report")
    public Map<String, Object> report(@RequestBody Map<String, Object> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (body == null || !body.containsKey("elderId")) {
                return R.error("参数缺失：elderId");
            }
            Long huanzheId = Long.valueOf(String.valueOf(body.get("elderId")));
            String address = String.valueOf(body.getOrDefault("address", ""));
            Double latitude = body.get("latitude") == null ? null : Double.valueOf(String.valueOf(body.get("latitude")));
            Double longitude = body.get("longitude") == null ? null : Double.valueOf(String.valueOf(body.get("longitude")));
            Date reportTime;
            Object t = body.get("time");
            if (t instanceof Number) {
                reportTime = new Date(((Number) t).longValue());
            } else if (t instanceof String) {
                try { reportTime = new Date(Long.parseLong((String) t)); } catch (Exception e) { reportTime = new Date(); }
            } else {
                reportTime = new Date();
            }

            LocationReport r = new LocationReport();
            r.setId(System.currentTimeMillis());
            r.setHuanzheId(huanzheId);
            r.setAddress(address);
            r.setLatitude(latitude);
            r.setLongitude(longitude);
            r.setReportTime(reportTime);
            r.setCreatedAt(new Date());
            locationReportService.insert(r);

            Map<String, Object> data = new HashMap<>();
            data.put("id", r.getId());
            data.put("elderId", r.getHuanzheId());
            data.put("address", r.getAddress());
            data.put("latitude", r.getLatitude());
            data.put("longitude", r.getLongitude());
            data.put("time", r.getReportTime());
            return R.ok().put("data", data);
        } catch (Exception e) {
            return R.error("位置保存失败：" + e.getMessage());
        }
    }

    @GetMapping("/latest")
    public Map<String, Object> latest(@RequestParam("elderId") Long elderId) {
        try {
            LocationReport r = locationReportService.getLatestByHuanzheId(elderId);
            if (r == null) {
                return R.error("暂无位置记录");
            }
            Map<String, Object> data = new HashMap<>();
            data.put("id", r.getId());
            data.put("elderId", r.getHuanzheId());
            data.put("address", r.getAddress());
            data.put("latitude", r.getLatitude());
            data.put("longitude", r.getLongitude());
            data.put("time", r.getReportTime());
            return R.ok().put("data", data);
        } catch (Exception e) {
            return R.error("查询失败：" + e.getMessage());
        }
    }

    @GetMapping("/history")
    public Map<String, Object> history(@RequestParam("elderId") Long elderId,
                                       @RequestParam(value = "limit", required = false) Integer limit) {
        try {
            int lim = (limit == null || limit <= 0) ? 50 : limit;
            List<LocationReport> list = locationReportService.selectList(
                    new com.baomidou.mybatisplus.mapper.EntityWrapper<LocationReport>()
                            .eq("huanzhe_id", elderId)
                            .orderBy("report_time", false)
            );
            List<Map<String, Object>> data = new ArrayList<>();
            for (int i = 0; i < list.size() && i < lim; i++) {
                LocationReport r = list.get(i);
                Map<String, Object> m = new HashMap<>();
                m.put("id", r.getId());
                m.put("elderId", r.getHuanzheId());
                m.put("address", r.getAddress());
                m.put("latitude", r.getLatitude());
                m.put("longitude", r.getLongitude());
                m.put("time", r.getReportTime());
                data.add(m);
            }
            return R.ok().put("data", data);
        } catch (Exception e) {
            return R.error("查询失败：" + e.getMessage());
        }
    }
}