package com.vutran0943.payment_service.factory;

import com.vutran0943.payment_service.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PaymentServiceFactory {
    private final Map<String, PaymentService> paymentServices;

    @Autowired
    public PaymentServiceFactory(List<PaymentService> paymentServices) {
        this.paymentServices = paymentServices.stream()
                .collect(Collectors.toMap(s -> s.getClass().getSimpleName(), s -> s));
    }

    public PaymentService getPaymentService(String provider) {
        return paymentServices.get(provider);
    }
}
