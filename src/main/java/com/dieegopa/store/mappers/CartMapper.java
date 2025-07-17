package com.dieegopa.store.mappers;

import com.dieegopa.store.dtos.CartDto;
import com.dieegopa.store.dtos.CartItemDto;
import com.dieegopa.store.entities.Cart;
import com.dieegopa.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toCartDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toCartItemDto(CartItem cartItem);
}
