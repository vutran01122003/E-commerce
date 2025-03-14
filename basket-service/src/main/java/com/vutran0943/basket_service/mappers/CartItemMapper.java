package com.vutran0943.basket_service.mappers;

import com.vutran0943.basket_service.dto.request.CartItemCreationRequest;
import com.vutran0943.basket_service.dto.response.CartItemResponse;
import com.vutran0943.basket_service.entities.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    public CartItemResponse toCartItemResponse(CartItem cartItem);
    public CartItem toCartItem(CartItemCreationRequest request);
}
