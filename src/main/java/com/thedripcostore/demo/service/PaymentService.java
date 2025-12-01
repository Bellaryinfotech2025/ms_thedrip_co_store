package com.thedripcostore.demo.service;

import com.thedripcostore.demo.dto.PaymentRequestDTO;
import com.thedripcostore.demo.dto.PaymentResponseDTO;

public interface PaymentService {

    // 1️⃣ Create a Razorpay Order
    PaymentResponseDTO createRazorpayOrder(PaymentRequestDTO requestDTO);

    // 2️⃣ Verify Payment Signature
    String verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature);

}
