package com.vutran0943.basket_service.controllers;

import com.vutran0943.basket_service.dto.request.CartItemCreationRequest;
import com.vutran0943.basket_service.dto.request.UpdateQuantityItemRequest;
import com.vutran0943.basket_service.dto.response.ApiResponse;
import com.vutran0943.basket_service.dto.response.CartItemResponse;
import com.vutran0943.basket_service.dto.response.CartResponse;
import com.vutran0943.basket_service.entities.Cart;
import com.vutran0943.basket_service.entities.CartItem;
import com.vutran0943.basket_service.services.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/carts")
public class CartController {
    private final CartService cartService;

    @PostMapping("/cart-items")
    public ApiResponse<CartItemResponse> addItemToCart(@Valid @RequestBody CartItemCreationRequest cartItemCreationRequest) {
        CartItemResponse response = cartService.addItemToCart(cartItemCreationRequest);

        ApiResponse<CartItemResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully added item to the cart");
        apiResponse.setData(response);

        return apiResponse;
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    public ApiResponse removeItemFromCart(@PathVariable String cartItemId) {
        cartService.removeItemFromCart(cartItemId);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Remove the item from the cart successfully");
        apiResponse.setData(null);

        return apiResponse;
    }

    @GetMapping("/users/{userId}")
    public ApiResponse<CartResponse> getCartsByUserId(@PathVariable String userId) {
        CartResponse res = cartService.getCartByUserId(userId);

        ApiResponse<CartResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully retrieved carts");
        apiResponse.setData(res);

        return apiResponse;
    }

    @PatchMapping("/cart-items/{cartItemId}")
    public ApiResponse<CartItem> updateCartItemQuantity(@PathVariable String cartItemId, @Valid @RequestBody UpdateQuantityItemRequest request) {
        CartItem cartItem = cartService.changeItemQuantity(cartItemId, request.getQuantity());

        ApiResponse<CartItem> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Successfully updated quantity");
        apiResponse.setData(cartItem);

        return apiResponse;
    }
}
