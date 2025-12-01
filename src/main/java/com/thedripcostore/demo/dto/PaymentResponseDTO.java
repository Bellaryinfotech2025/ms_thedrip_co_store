package com.thedripcostore.demo.dto;


public class PaymentResponseDTO {

    private String razorpayOrderId;   // order_xyz123
    private String razorpayKey;       // public key to send to frontend
    private Double amount;            // amount (INR)
    private String currency;          // usually "INR"

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public String getRazorpayKey() {
        return razorpayKey;
    }

    public void setRazorpayKey(String razorpayKey) {
        this.razorpayKey = razorpayKey;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
