package com.vutran0943.delivery_service.dtos.responses;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentResponse {
    private String id;
    private String orderId;
    private String recipientName;
    private String recipientPhone;
    private String address;
    private double weight;
    private double totalCost;
    private String carrier;
    private String status;
}
