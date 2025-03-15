package com.vutran0943.delivery_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "shipments")
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "order_id", nullable = false)
    private String orderId;
    @Column(name="recipient_name", nullable = false)
    private String recipientName;
    @Column(name="recipient_phone", nullable = false)
    private String recipientPhone;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private double weight;
    @Column(nullable = false)
    private double totalCost;
    @Column(nullable = false)
    private String carrier;
    @Column(nullable = false)
    private String status;
    @Column(name = "estimated_delivery_at")
    private LocalDateTime estimatedDeliveryAt;
    @Column(name = "shipped_at")
    private LocalDateTime shippedAt;
    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;
}
