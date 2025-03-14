package com.vutran0943.basket_service.repositories;

import com.vutran0943.basket_service.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    public CartItem getCartItemById(String id);
    public CartItem getCartItemByCartIdAndProductId(String cartId, String productId);
    public List<CartItem> getCartItemByCartId(String cartId);
}
