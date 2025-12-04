package com.thedripcostore.demo.service;

import com.thedripcostore.demo.dto.CreateRazorpayOrderRequest;
import com.thedripcostore.demo.dto.RazorpayWebhookRequest;
import com.thedripcostore.demo.entity.PaymentDetail;
import java.util.Map;
import java.util.List;

public interface PaymentService {
    Map<String, Object> createRazorpayOrder(CreateRazorpayOrderRequest request);
    PaymentDetail verifyPayment(RazorpayWebhookRequest webhookRequest);
    PaymentDetail getPaymentByPaymentId(String paymentId);
    PaymentDetail getPaymentByRazorpayOrderId(String razorpayOrderId);
    List<PaymentDetail> getPaymentsByUserId(String userId);
    PaymentDetail getPaymentByOrderId(String orderId);
    
     
    
    List<PaymentDetail> getAllPayments();
}
