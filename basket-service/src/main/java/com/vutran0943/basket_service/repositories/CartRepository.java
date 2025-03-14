package com.vutran0943.basket_service.repositories;

import com.vutran0943.basket_service.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    public Cart getCartByUserIdAndStatus(String userId, String status);
    public Cart getCartById(String cartId);
}
