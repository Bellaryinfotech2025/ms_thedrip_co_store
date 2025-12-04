package com.thedripcostore.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.thedripcostore.demo.entity.PaymentDetail;
import java.util.Optional;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentDetail, Long> {
    Optional<PaymentDetail> findByPaymentId(String paymentId);
    Optional<PaymentDetail> findByRazorpayOrderId(String razorpayOrderId);
    List<PaymentDetail> findByUserId(String userId);
    Optional<PaymentDetail> findByOrderId(String orderId);
    
    
}
