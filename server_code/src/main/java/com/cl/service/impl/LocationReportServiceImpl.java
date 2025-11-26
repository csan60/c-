package com.cl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.dao.LocationReportDao;
import com.cl.entity.LocationReport;
import com.cl.service.LocationReportService;
import org.springframework.stereotype.Service;

@Service("locationReportService")
public class LocationReportServiceImpl extends ServiceImpl<LocationReportDao, LocationReport> implements LocationReportService {
    @Override
    public LocationReport getLatestByHuanzheId(Long huanzheId) {
        return this.selectOne(new EntityWrapper<LocationReport>()
                .eq("huanzhe_id", huanzheId)
                .orderBy("report_time", false));
    }
}