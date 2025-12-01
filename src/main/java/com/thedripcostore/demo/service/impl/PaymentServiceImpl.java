package com.thedripcostore.demo.service.impl;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.thedripcostore.demo.entity.PaymentEntity;
import com.razorpay.RazorpayClient;
import com.thedripcostore.demo.dto.PaymentRequestDTO;
import com.thedripcostore.demo.dto.PaymentResponseDTO;
import com.thedripcostore.demo.repository.PaymentRepository;
import com.thedripcostore.demo.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${razorpay.key}")
    private String razorpayKey;

    @Value("${razorpay.secret}")
    private String razorpaySecret;

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentResponseDTO createRazorpayOrder(PaymentRequestDTO requestDTO) {
        try {
            RazorpayClient client = new RazorpayClient(razorpayKey, razorpaySecret);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", requestDTO.getAmount() * 100);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "rcpt_" + System.currentTimeMillis());

            Order razorpayOrder = client.orders.create(orderRequest);

            // Save payment in DB
            PaymentEntity payment = new PaymentEntity();
            payment.setOrderId(requestDTO.getOrderId());
            payment.setAmount(requestDTO.getAmount());
            payment.setRazorpayOrderId(razorpayOrder.get("id"));
            payment.setStatus("PENDING"); // initial state

            paymentRepository.save(payment);

            // Return response to frontend
            PaymentResponseDTO response = new PaymentResponseDTO();
            response.setRazorpayOrderId(razorpayOrder.get("id"));
            response.setRazorpayKey(razorpayKey);
            response.setAmount(requestDTO.getAmount());
            response.setCurrency("INR");

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Error while creating Razorpay order: " + e.getMessage());
        }
    }




    @Override
    public String verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
        try {
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", razorpayOrderId);
            attributes.put("razorpay_payment_id", razorpayPaymentId);
            attributes.put("razorpay_signature", razorpaySignature);

            boolean isValid = com.razorpay.Utils.verifyPaymentSignature(attributes, razorpaySecret);

            if (isValid) {
                PaymentEntity payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId);
                payment.setRazorpayPaymentId(razorpayPaymentId);
                payment.setRazorpaySignature(razorpaySignature);
                payment.setStatus("PAID");
                paymentRepository.save(payment);

                return "Payment Verified Successfully";
            }

            // If invalid signature
            PaymentEntity payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId);
            payment.setStatus("FAILED");
            paymentRepository.save(payment);

            return "Invalid Payment Signature";

        } catch (Exception e) {

            PaymentEntity payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId);
            payment.setStatus("FAILED");
            paymentRepository.save(payment);

            return "Payment Verification Failed: " + e.getMessage();
        }
    }


}

