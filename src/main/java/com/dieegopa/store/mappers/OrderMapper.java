package com.dieegopa.store.mappers;

import com.dieegopa.store.dtos.OrderDto;
import com.dieegopa.store.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
}
