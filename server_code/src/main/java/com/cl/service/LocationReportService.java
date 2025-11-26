package com.cl.service;

import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.LocationReport;

public interface LocationReportService extends IService<LocationReport> {
    LocationReport getLatestByHuanzheId(Long huanzheId);
}