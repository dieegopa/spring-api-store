package com.dieegopa.store.mappers;

import com.dieegopa.store.dtos.RegisterUserRequest;
import com.dieegopa.store.dtos.UpdateUserRequest;
import com.dieegopa.store.dtos.UserDto;
import com.dieegopa.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(RegisterUserRequest registerUserRequest);

    void update(UpdateUserRequest updateUserRequest, @MappingTarget User user);
}