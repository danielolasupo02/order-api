package com.example.orderapi.config;

import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpMetricsConfig {

    @Bean
    public MeterFilter httpMetricsFilter() {
        // Allow all metrics that start with http.server.requests OR order_
        return MeterFilter.deny(id ->
                !id.getName().startsWith("http.server.requests") &&
                        !id.getName().startsWith("order_")
        );
    }
}