package com.vutran0943.basket_service.mappers;

import com.vutran0943.basket_service.dto.response.CartResponse;
import com.vutran0943.basket_service.entities.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    public CartResponse toCartResponse(Cart cart);
}
