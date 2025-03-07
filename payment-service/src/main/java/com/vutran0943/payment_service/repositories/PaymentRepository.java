package com.vutran0943.payment_service.repositories;

import com.vutran0943.payment_service.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    Payment findPaymentById(String paymentId);
}
