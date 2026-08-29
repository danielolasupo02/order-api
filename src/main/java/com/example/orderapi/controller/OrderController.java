package com.example.orderapi.controller;

import com.example.orderapi.dto.OrderResponse;
import com.example.orderapi.service.OrderService;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private static final String ENDPOINT = "/api/orders";
    private static final String METHOD = "GET";

    private final OrderService orderService;
    private final Counter requestsTotal;
    private final Timer orderRequestTimer;


    public OrderController(OrderService orderService, MeterRegistry registry) {
        this.orderService = orderService;

        // 3. Request volume
        this.requestsTotal = Counter.builder("order_requests_total")
                .description("Total number of order requests received")
                .tag("method", METHOD)
                .tag("endpoint", ENDPOINT)
                .register(registry);

        // Create timer programmatically
        this.orderRequestTimer = Timer.builder("order_request_latency_seconds")
                .description("Time taken to process order requests")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .sla(Duration.ofMillis(10), Duration.ofMillis(50), Duration.ofMillis(100),
                        Duration.ofMillis(200), Duration.ofMillis(500), Duration.ofSeconds(1))
                .minimumExpectedValue(Duration.ofMillis(10))
                .maximumExpectedValue(Duration.ofSeconds(2))
                .register(registry);
    }

    @GetMapping("/api/orders")
    @Timed(value = "order.api.requests", histogram = true,
            percentiles = {0.5, 0.95, 0.99})
    public ResponseEntity<OrderResponse> getOrder(
            @RequestParam(name = "fail", defaultValue = "false") boolean fail) {
        log.info("Received request GET /api/orders [fail={}]", fail);
        requestsTotal.increment();

        // Record the time using the timer
        return orderRequestTimer.record(() -> {
            OrderResponse response = orderService.processOrder(fail);
            return ResponseEntity.ok(response);
        });
    }
}