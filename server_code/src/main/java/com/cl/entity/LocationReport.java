package com.cl.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.Date;

@TableName("location_report")
public class LocationReport implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;
    private Long huanzheId;
    private String address;
    private Double latitude;
    private Double longitude;
    private Date reportTime;
    private Date createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getHuanzheId() { return huanzheId; }
    public void setHuanzheId(Long huanzheId) { this.huanzheId = huanzheId; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Date getReportTime() { return reportTime; }
    public void setReportTime(Date reportTime) { this.reportTime = reportTime; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}