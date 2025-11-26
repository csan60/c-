package com.cl.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeocodeConfig {

    @Value("${geocode.provider:qq}")
    private String provider;

    @Value("${geocode.qq.key:}")
    private String qqKey;

    @Value("${geocode.amap.key:}")
    private String amapKey;

    public String getProvider() { return provider; }
    public String getQqKey() { return qqKey; }
    public String getAmapKey() { return amapKey; }
}