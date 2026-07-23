package com.example.orderapi.dto;

public class OrderResponse {

    private String status;
    private String orderId;
    private int amount;

    public OrderResponse() {
    }

    public OrderResponse(String status, String orderId, int amount) {
        this.status = status;
        this.orderId = orderId;
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
