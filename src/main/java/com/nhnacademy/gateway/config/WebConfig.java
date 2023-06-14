package com.nhnacademy.gateway.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class WebConfig {
    @Bean // connection timeout : 10초, read timeout : 10초
    public RestTemplate restTemplate(RestTemplateBuilder builder)
    {
        return builder.setReadTimeout(Duration.ofSeconds(10L))
                .setConnectTimeout(Duration.ofSeconds(10L))
                .build();
    }
}
