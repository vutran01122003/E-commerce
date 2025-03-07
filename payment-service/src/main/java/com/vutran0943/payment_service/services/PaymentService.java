package com.vutran0943.payment_service.services;

import com.vutran0943.payment_service.dto.request.PaymentCreationRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {
    String processPayment(PaymentCreationRequest paymentCreationRequest, HttpServletRequest httpRequest) throws Exception;
    String inspectPaymentStatus(String paymentId, HttpServletRequest http) throws Exception;
    String discardPayment(String paymentId, HttpServletRequest http) throws Exception;
}

