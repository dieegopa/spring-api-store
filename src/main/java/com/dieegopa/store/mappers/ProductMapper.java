package com.dieegopa.store.mappers;

import com.dieegopa.store.dtos.ProductDto;
import com.dieegopa.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toDto(Product product);
}
