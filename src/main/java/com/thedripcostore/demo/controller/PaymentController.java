package com.thedripcostore.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.thedripcostore.demo.dto.CreateRazorpayOrderRequest;
import com.thedripcostore.demo.dto.RazorpayWebhookRequest;
import com.thedripcostore.demo.entity.PaymentDetail;
import com.thedripcostore.demo.service.PaymentService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<Map<String, Object>> createRazorpayOrder(@RequestBody CreateRazorpayOrderRequest request) {
        try {
            logger.info("Received POST request to create Razorpay order for userId: {}", request.getUserId());
            logger.info("[DEBUG] Request payload - Amount: {}, Currency: {}", request.getAmount(), request.getCurrency());
            
            Map<String, Object> orderData = paymentService.createRazorpayOrder(request);
            logger.info("Razorpay order created and returned to frontend");
            return ResponseEntity.ok(orderData);
        } catch (Exception e) {
            logger.error("Error creating Razorpay order: {} | Full Error: {}", e.getMessage(), e.toString(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error_type", e.getClass().getSimpleName());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/verify-payment")
    public ResponseEntity<Map<String, Object>> verifyPayment(@RequestBody RazorpayWebhookRequest webhookRequest) {
        try {
            logger.info("Received payment verification request - paymentId: {}, orderId: {}", 
                webhookRequest.getRazorpay_payment_id(), webhookRequest.getRazorpay_order_id());

            PaymentDetail payment = paymentService.verifyPayment(webhookRequest);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Payment verified successfully");
            response.put("paymentId", payment.getPaymentId());
            response.put("status", payment.getStatus());

            logger.info("Payment verified successfully: {}", payment.getPaymentId());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error verifying payment: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(400).body(errorResponse);
        }
    }

    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<PaymentDetail> getPaymentByPaymentId(@PathVariable String paymentId) {
        try {
            logger.info("Received GET request for payment: {}", paymentId);
            PaymentDetail payment = paymentService.getPaymentByPaymentId(paymentId);
            logger.info("Payment retrieved successfully: {}", paymentId);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            logger.error("Error fetching payment: {}", e.getMessage(), e);
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/razorpay-order/{razorpayOrderId}")
    public ResponseEntity<PaymentDetail> getPaymentByRazorpayOrderId(@PathVariable String razorpayOrderId) {
        try {
            logger.info("Received GET request for razorpay order: {}", razorpayOrderId);
            PaymentDetail payment = paymentService.getPaymentByRazorpayOrderId(razorpayOrderId);
            logger.info("Payment retrieved successfully for razorpay order: {}", razorpayOrderId);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            logger.error("Error fetching payment by razorpay order: {}", e.getMessage(), e);
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentDetail>> getPaymentsByUserId(@PathVariable String userId) {
        try {
            logger.info("Received GET request for all payments of userId: {}", userId);
            List<PaymentDetail> payments = paymentService.getPaymentsByUserId(userId);
            logger.info("Fetched {} payments for userId: {}", payments.size(), userId);
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            logger.error("Error fetching payments for userId: {}", userId, e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentDetail> getPaymentByOrderId(@PathVariable String orderId) {
        try {
            logger.info("Received GET request for payment with orderId: {}", orderId);
            PaymentDetail payment = paymentService.getPaymentByOrderId(orderId);
            logger.info("Payment retrieved successfully for orderId: {}", orderId);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            logger.error("Error fetching payment by orderId: {}", orderId, e);
            return ResponseEntity.status(404).build();
        }
    }
    
    
    
    
    
    
    @GetMapping("/all")
    public ResponseEntity<List<PaymentDetail>> getAllPayments() {
        try {
            logger.info("Received GET request for all payments");
            List<PaymentDetail> payments = paymentService.getAllPayments();
            logger.info("Fetched {} payments", payments.size());
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            logger.error("Error fetching all payments", e);
            return ResponseEntity.status(500).build();
        }
    }
}
