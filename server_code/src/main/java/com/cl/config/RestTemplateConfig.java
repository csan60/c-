package com.cl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Configuration
public class RestTemplateConfig {
    
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        
        // 设置连接超时时间（毫秒）
        factory.setConnectTimeout(30000); // 30秒
        // 设置读取超时时间（毫秒）
        factory.setReadTimeout(60000); // 60秒
        
        RestTemplate restTemplate = new RestTemplate(factory);
        
        // 设置UTF-8编码的字符串消息转换器
        restTemplate.getMessageConverters()
            .set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        
        return restTemplate;
    }
}