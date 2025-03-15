package com.vutran0943.delivery_service.dtos.requests;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentCreationRequest {
    @NotBlank(message = "Order id is required")
    private String orderId;
    @NotBlank(message = "Recipient name is required")
    private String recipientName;
    @NotBlank(message = "Recipient phone is required")
    private String recipientPhone;
    @NotBlank(message = "Address is required")
    private String address;
    @NotNull(message = "Weight is required")
    @Min(value = 0, message = "Weight must be greater than 0")
    private double weight;
    @NotNull(message = "Total cost is required")
    @Min(value = 0, message = "Total cost must be greater than or equal to 0")
    private double totalCost;
    private String carrier;
}
