package com.thedripcostore.demo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import com.thedripcostore.demo.dto.CreateRazorpayOrderRequest;
import com.thedripcostore.demo.dto.RazorpayWebhookRequest;
import com.thedripcostore.demo.entity.PaymentDetail;
import com.thedripcostore.demo.repository.PaymentRepository;
import com.thedripcostore.demo.service.PaymentService;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public Map<String, Object> createRazorpayOrder(CreateRazorpayOrderRequest request) {
        try {
            logger.info("Creating Razorpay order for userId: {}, amount: {}", request.getUserId(), request.getAmount());

            logger.info("[v0] DEBUG - Razorpay Key ID: {}", razorpayKeyId != null ? razorpayKeyId.substring(0, 8) + "***" : "NULL");
            logger.info("[v0] DEBUG - Razorpay Key Secret loaded: {}", razorpayKeySecret != null ? "YES" : "NO");

            if (request.getUserId() == null || request.getAmount() == null) {
                throw new RuntimeException("userId and amount are required");
            }
            
            if (razorpayKeyId == null || razorpayKeyId.isEmpty() || razorpayKeySecret == null || razorpayKeySecret.isEmpty()) {
                logger.error("Razorpay keys are not configured. Check application.properties");
                throw new RuntimeException("Razorpay API keys not configured in application.properties");
            }

            logger.info("[v0] DEBUG - Initializing RazorpayClient with key: {}", razorpayKeyId.substring(0, Math.min(15, razorpayKeyId.length())));
            RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

            // Amount in paise (multiply by 100)
            long amountInPaise = (long) (request.getAmount() * 100);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", request.getCurrency());
            orderRequest.put("receipt", "receipt#" + System.currentTimeMillis());

            // Customer notes
            JSONObject notes = new JSONObject();
            notes.put("userId", request.getUserId());
            notes.put("customerName", request.getCustomerName());
            notes.put("customerEmail", request.getCustomerEmail());
            notes.put("customerPhone", request.getCustomerPhone());
            orderRequest.put("notes", notes);

            logger.info("[v0] DEBUG - Creating order with request: {}", orderRequest.toString());
            com.razorpay.Order razorpayOrder = razorpay.orders.create(orderRequest);
            String razorpayOrderId = razorpayOrder.get("id");

            logger.info("Razorpay order created successfully: {}", razorpayOrderId);

            String orderId = request.getOrderId() != null ? request.getOrderId() : "ORD-" + System.currentTimeMillis();

            // Save payment detail to database
            PaymentDetail payment = new PaymentDetail();
            payment.setOrderId(orderId);
            payment.setRazorpayOrderId(razorpayOrderId);
            payment.setUserId(request.getUserId());
            payment.setAmount(request.getAmount());
            payment.setCurrency(request.getCurrency());
            payment.setCustomerName(request.getCustomerName());
            payment.setCustomerEmail(request.getCustomerEmail());
            payment.setCustomerPhone(request.getCustomerPhone());
            payment.setStatus("created");
            payment.setCreatedAt(LocalDateTime.now());

            PaymentDetail savedPayment = paymentRepository.save(payment);
            logger.info("Payment detail saved to database for razorpayOrderId: {}", razorpayOrderId);

            // Prepare response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("razorpayOrderId", razorpayOrderId);
            response.put("orderId", orderId);
            response.put("amount", request.getAmount());
            response.put("currency", request.getCurrency());
            response.put("keyId", razorpayKeyId);
            response.put("customerName", request.getCustomerName());
            response.put("customerEmail", request.getCustomerEmail());
            response.put("customerPhone", request.getCustomerPhone());

            return response;

        } catch (RazorpayException e) {
           
            throw new RuntimeException("Failed to create Razorpay order: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error creating Razorpay order: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create order", e);
        }
    }

    @Override
    @Transactional
    public PaymentDetail verifyPayment(RazorpayWebhookRequest webhookRequest) {
        try {
            logger.info("Verifying payment - razorpay_payment_id: {}, razorpay_order_id: {}", 
                webhookRequest.getRazorpay_payment_id(), webhookRequest.getRazorpay_order_id());

            if (webhookRequest.getRazorpay_payment_id() == null || webhookRequest.getRazorpay_order_id() == null) {
                throw new RuntimeException("paymentId and orderId are required");
            }

            // Verify signature
            String generatedSignature = generateSignature(
                webhookRequest.getRazorpay_order_id(),
                webhookRequest.getRazorpay_payment_id(),
                razorpayKeySecret
            );

            if (!generatedSignature.equals(webhookRequest.getRazorpay_signature())) {
                logger.warn("Signature verification failed for payment: {}", webhookRequest.getRazorpay_payment_id());
                throw new RuntimeException("Signature verification failed");
            }

            logger.info("Signature verified successfully");

            // Find existing payment record
            Optional<PaymentDetail> existingPayment = paymentRepository.findByRazorpayOrderId(webhookRequest.getRazorpay_order_id());

            PaymentDetail payment;
            if (existingPayment.isPresent()) {
                payment = existingPayment.get();
                logger.info("Existing payment found, updating with verification details");
            } else {
                logger.error("Payment not found for razorpayOrderId: {}", webhookRequest.getRazorpay_order_id());
                throw new RuntimeException("Payment record not found for this order");
            }

            payment.setPaymentId(webhookRequest.getRazorpay_payment_id());
            payment.setSignature(webhookRequest.getRazorpay_signature());
            payment.setStatus("paid");
            payment.setPaymentMethod(webhookRequest.getMethod());

            PaymentDetail savedPayment = paymentRepository.save(payment);
            logger.info("Payment verified and saved successfully with status PAID: {}", savedPayment.getPaymentId());

            return savedPayment;

        } catch (Exception e) {
            logger.error("Error verifying payment: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to verify payment", e);
        }
    }

    @Override
    public PaymentDetail getPaymentByPaymentId(String paymentId) {
        try {
            logger.info("Fetching payment by paymentId: {}", paymentId);
            Optional<PaymentDetail> payment = paymentRepository.findByPaymentId(paymentId);

            if (payment.isEmpty()) {
                logger.warn("Payment not found: {}", paymentId);
                throw new RuntimeException("Payment not found");
            }

            logger.info("Payment fetched successfully: {}", paymentId);
            return payment.get();
        } catch (Exception e) {
            logger.error("Error fetching payment by paymentId: {}", paymentId, e);
            throw new RuntimeException("Failed to fetch payment", e);
        }
    }

    @Override
    public PaymentDetail getPaymentByRazorpayOrderId(String razorpayOrderId) {
        try {
            logger.info("Fetching payment by razorpayOrderId: {}", razorpayOrderId);
            Optional<PaymentDetail> payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId);

            if (payment.isEmpty()) {
                logger.warn("Payment not found for razorpayOrderId: {}", razorpayOrderId);
                throw new RuntimeException("Payment not found");
            }

            logger.info("Payment fetched successfully: {}", razorpayOrderId);
            return payment.get();
        } catch (Exception e) {
            logger.error("Error fetching payment by razorpayOrderId: {}", razorpayOrderId, e);
            throw new RuntimeException("Failed to fetch payment", e);
        }
    }

    @Override
    public List<PaymentDetail> getPaymentsByUserId(String userId) {
        try {
            logger.info("Fetching all payments for userId: {}", userId);
            List<PaymentDetail> payments = paymentRepository.findByUserId(userId);
            logger.info("Fetched {} payments for userId: {}", payments.size(), userId);
            return payments;
        } catch (Exception e) {
            logger.error("Error fetching payments for userId: {}", userId, e);
            throw new RuntimeException("Failed to fetch payments", e);
        }
    }

    @Override
    public PaymentDetail getPaymentByOrderId(String orderId) {
        try {
            logger.info("Fetching payment by orderId: {}", orderId);
            Optional<PaymentDetail> payment = paymentRepository.findByOrderId(orderId);

            if (payment.isEmpty()) {
                logger.warn("Payment not found for orderId: {}", orderId);
                throw new RuntimeException("Payment not found");
            }

            logger.info("Payment fetched successfully for orderId: {}", orderId);
            return payment.get();
        } catch (Exception e) {
            logger.error("Error fetching payment by orderId: {}", orderId, e);
            throw new RuntimeException("Failed to fetch payment", e);
        }
    }

    // Helper method to generate signature
    private String generateSignature(String orderId, String paymentId, String keySecret) {
        try {
            String data = orderId + "|" + paymentId;
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(keySecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);
            byte[] signature = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder result = new StringBuilder();
            for (byte b : signature) {
                result.append(String.format("%02x", b));
            }
            return result.toString();
        } catch (Exception e) {
            logger.error("Error generating signature: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate signature", e);
        }
    }
    
    
    
    
    
    
    
    
    @Override
    public List<PaymentDetail> getAllPayments() {
        try {
            logger.info("Fetching all payments from database");
            List<PaymentDetail> payments = paymentRepository.findAll();
            logger.info("Fetched {} payments", payments.size());
            return payments;
        } catch (Exception e) {
            logger.error("Error fetching all payments", e);
            throw new RuntimeException("Failed to fetch all payments", e);
        }
    }

     
}
