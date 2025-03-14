package com.vutran0943.basket_service.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;
    @Column(name = "product_id", nullable = false)
    private String productId;
    @Column(name = "shop_id", nullable = false)
    private String shopId;
    private int quantity;
    private double price;
}
