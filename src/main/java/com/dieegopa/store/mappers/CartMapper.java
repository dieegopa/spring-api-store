package com.dieegopa.store.mappers;

import com.dieegopa.store.dtos.CartDto;
import com.dieegopa.store.entities.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toCartDto(Cart cart);
}
