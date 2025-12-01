package com.thedripcostore.demo.controller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thedripcostore.demo.dto.PaymentRequestDTO;
import com.thedripcostore.demo.dto.PaymentResponseDTO;
import com.thedripcostore.demo.service.PaymentService;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // 1️⃣ Create Razorpay Order
    @PostMapping("/create-order")
    public PaymentResponseDTO createOrder(@RequestBody PaymentRequestDTO requestDTO) {
        return paymentService.createRazorpayOrder(requestDTO);
    }

    // 2️⃣ Verify Payment
    @PostMapping("/verify")
    public String verifyPayment(
            @RequestParam String razorpay_order_id,
            @RequestParam String razorpay_payment_id,
            @RequestParam String razorpay_signature
    ) {
        return paymentService.verifyPayment(razorpay_order_id, razorpay_payment_id, razorpay_signature);
    }
}
