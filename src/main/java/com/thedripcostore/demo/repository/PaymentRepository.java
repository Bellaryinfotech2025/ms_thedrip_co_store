package com.thedripcostore.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.thedripcostore.demo.entity.PaymentEntity;


@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    PaymentEntity findByRazorpayOrderId(String razorpayOrderId);

    PaymentEntity findByPaymentId(String paymentId);

}
