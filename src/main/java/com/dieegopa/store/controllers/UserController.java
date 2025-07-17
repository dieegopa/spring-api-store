package com.dieegopa.store.controllers;

import com.dieegopa.store.dtos.ChangePasswordRequest;
import com.dieegopa.store.dtos.RegisterUserRequest;
import com.dieegopa.store.dtos.UpdateUserRequest;
import com.dieegopa.store.dtos.UserDto;
import com.dieegopa.store.entities.Role;
import com.dieegopa.store.mappers.UserMapper;
import com.dieegopa.store.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Tag(name = "User", description = "Operations related to user management")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    @Operation(
            summary = "Get all users",
            description = "Retrieves a list of all registered users, sorted by the specified field."
    )
    public Iterable<UserDto> getAllUsers(
            @Parameter(
                    description = "Field to sort users by. Default is 'name'.",
                    example = "name"
            )
            @RequestParam(required = false, defaultValue = "", name = "sort") String sort
    ) {

        if (!Set.of("name", "email").contains(sort)) {
            sort = "name";
        }

        return userRepository.findAll(Sort.by(sort))
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by ID",
            description = "Retrieves a user by their unique ID. Returns 404 if the user does not exist."
    )
    public ResponseEntity<UserDto> getUser(
            @Parameter(
                    description = "The unique ID of the user to retrieve",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        var user = userRepository.findById(id).orElse(null);

        if (user == null) {
            // new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping
    @Operation(
            summary = "Register a new user",
            description = "Registers a new user in the system. The email must be unique."
    )
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder
    ) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(
                    Map.of("email", "Email already exists")
            );
        }

        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        var userDto = userMapper.toDto(user);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();

        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update user details",
            description = "Updates the details of an existing user. Returns 404 if the user does not exist."
    )
    public ResponseEntity<UserDto> updateUser(
            @Parameter(
                    description = "The unique ID of the user to update",
                    required = true,
                    example = "1"
            )
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateUserRequest request
    ) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        userMapper.update(request, user);
        userRepository.save(user);

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete user",
            description = "Deletes a user by their unique ID. Returns 204 if the user was successfully deleted, or 404 if the user does not exist."
    )
    public ResponseEntity<Void> deleteUser(
            @Parameter(
                    description = "The unique ID of the user to delete",
                    required = true,
                    example = "1"
            )
            @PathVariable(name = "id") Long id
    ) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    @Operation(
            summary = "Change user password",
            description = "Changes the password of an existing user. Returns 204 if successful, or 404 if the user does not exist."
    )
    public ResponseEntity<Void> changePassword(
            @Parameter(
                    description = "The unique ID of the user whose password is to be changed",
                    required = true,
                    example = "1"
            )
            @PathVariable(name = "id") Long id,
            @RequestBody ChangePasswordRequest request
    ) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (!user.getPassword().equals(request.getOldPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }

}
