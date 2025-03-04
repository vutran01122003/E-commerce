package com.vutran0943.payment_service.mappers;

import com.vutran0943.payment_service.dto.request.PaymentCreationRequest;
import com.vutran0943.payment_service.entities.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    Payment toPayment(PaymentCreationRequest paymentCreationRequest);
}
