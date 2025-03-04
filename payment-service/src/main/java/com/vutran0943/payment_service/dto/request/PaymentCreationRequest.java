package com.vutran0943.payment_service.dto.request;

import com.vutran0943.payment_service.shared.PaymentMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCreationRequest {
    @NotBlank(message = "User ID must be required")
    private String userId;
    @NotNull(message = "Payment method must be required")
    private PaymentMethod paymentMethod;
    @NotBlank(message = "Order ID must be required")
    private String orderId;
    @NotNull(message = "Amount must be required")
    private Double amount;
    @NotBlank(message = "Currency must be required")
    private String currency;
    @Length(max = 255, message = "Description must be less than 256 characters")
    private String description;
    private String bankCode;
    private String provider;
}
