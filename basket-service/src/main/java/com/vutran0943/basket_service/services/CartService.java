package com.vutran0943.basket_service.services;

import com.vutran0943.basket_service.dto.request.CartItemCreationRequest;
import com.vutran0943.basket_service.dto.response.CartItemResponse;
import com.vutran0943.basket_service.dto.response.CartResponse;
import com.vutran0943.basket_service.entities.Cart;
import com.vutran0943.basket_service.entities.CartItem;
import com.vutran0943.basket_service.mappers.CartItemMapper;
import com.vutran0943.basket_service.mappers.CartMapper;
import com.vutran0943.basket_service.repositories.CartItemRepository;
import com.vutran0943.basket_service.repositories.CartRepository;
import com.vutran0943.basket_service.shared.CartStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;

    private Cart createCart (String userId) {
        Cart cart = cartRepository.getCartByUserIdAndStatus(userId, CartStatus.ACTIVE.toString());

        if(cart != null) return cart;

        cart = Cart.builder()
                .userId(userId)
                .status(CartStatus.ACTIVE.toString())
                .build();

        return cartRepository.save(cart);
    }

    public CartItemResponse addItemToCart(CartItemCreationRequest request) {
        System.out.print(request);
        Cart cart = createCart(request.getUserId());

        CartItem existedItem = cartItemRepository.getCartItemByCartIdAndProductId(cart.getId(), request.getProductId());

        if(existedItem == null) {
            CartItem cartItem  = cartItemMapper.toCartItem(request);
            cartItem.setCart(cart);
            return cartItemMapper.toCartItemResponse(cartItemRepository.save(cartItem));
        }

        existedItem.setQuantity(existedItem.getQuantity() + request.getQuantity());
        existedItem.setPrice(request.getPrice());

        return cartItemMapper.toCartItemResponse(cartItemRepository.save(existedItem));
    }

    public void removeItemFromCart(String cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public CartResponse getCartByUserId(String userId) {
        Cart cart =  cartRepository.getCartByUserIdAndStatus(userId, CartStatus.ACTIVE.toString());
        return cartMapper.toCartResponse(cart);
    }

    public CartItem changeItemQuantity(String cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.getCartItemById(cartItemId);
        cartItem.setQuantity(quantity);

        return cartItemRepository.save(cartItem);
    }
}
