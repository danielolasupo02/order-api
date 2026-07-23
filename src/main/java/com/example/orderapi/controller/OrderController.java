package com.example.orderapi.controller;

import com.example.orderapi.dto.OrderResponse;
import com.example.orderapi.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<OrderResponse> getOrder(
            @RequestParam(name = "fail", defaultValue = "false") boolean fail) {
        log.info("Received request GET /api/orders [fail={}]", fail);
        OrderResponse response = orderService.processOrder(fail);
        return ResponseEntity.ok(response);
    }
}
