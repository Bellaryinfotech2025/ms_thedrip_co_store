package com.thedripcostore.demo.dto;


public class PaymentRequestDTO {

    private String orderId;   // Your internal orderId
    private Double amount;    // Total order amount (in INR, NOT paise)

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
