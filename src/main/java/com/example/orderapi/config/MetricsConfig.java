package com.example.orderapi.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class MetricsConfig {

    @Bean
    public Timer orderRequestTimer(MeterRegistry registry) {
        return Timer.builder("order_request_latency_seconds")
                .description("Time taken to process order requests")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .sla(Duration.ofMillis(10), Duration.ofMillis(50), Duration.ofMillis(100),
                        Duration.ofMillis(200), Duration.ofMillis(500), Duration.ofSeconds(1))
                .minimumExpectedValue(Duration.ofMillis(10))
                .maximumExpectedValue(Duration.ofSeconds(2))
                .register(registry);
    }
}