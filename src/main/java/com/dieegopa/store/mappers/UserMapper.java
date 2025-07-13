package com.dieegopa.store.mappers;

import com.dieegopa.store.dtos.UserDto;
import com.dieegopa.store.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}