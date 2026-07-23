package com.example.orderapi.service;

import com.example.orderapi.dto.OrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private static final int MIN_LATENCY_MS = 100;
    private static final int MAX_LATENCY_MS = 500;
    private static final int FIXED_AMOUNT = 2500;

    public OrderResponse processOrder(boolean fail) {
        String orderId = UUID.randomUUID().toString();
        int latency = ThreadLocalRandom.current().nextInt(MIN_LATENCY_MS, MAX_LATENCY_MS + 1);

        log.info("Processing order [orderId={}, fail={}, simulatedLatencyMs={}]", orderId, fail, latency);
        simulateLatency(latency);

        if (fail) {
            log.error("Order processing failed [orderId={}]: Payment gateway timeout", orderId);
            throw new RuntimeException("Payment gateway timeout");
        }

        OrderResponse response = new OrderResponse("SUCCESS", orderId, FIXED_AMOUNT);
        log.info("Order processed successfully [orderId={}, amount={}]", orderId, FIXED_AMOUNT);
        return response;
    }

    private void simulateLatency(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Latency simulation interrupted", e);
        }
    }
}
