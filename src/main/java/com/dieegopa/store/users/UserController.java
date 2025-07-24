package com.dieegopa.store.users;

import com.dieegopa.store.dtos.*;
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

import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Tag(name = "User", description = "Operations related to user management")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

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
        return userService.getAllUsers(sort);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by ID",
            description = "Retrieves a user by their unique ID. Returns 404 if the user does not exist."
    )
    public UserDto getUser(
            @Parameter(
                    description = "The unique ID of the user to retrieve",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        return userService.getUser(id);
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
        var userDto = userService.registerUser(request);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update user details",
            description = "Updates the details of an existing user. Returns 404 if the user does not exist."
    )
    public UserDto updateUser(
            @Parameter(
                    description = "The unique ID of the user to update",
                    required = true,
                    example = "1"
            )
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateUserRequest request
    ) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete user",
            description = "Deletes a user by their unique ID. Returns 204 if the user was successfully deleted, or 404 if the user does not exist."
    )
    public void deleteUser(
            @Parameter(
                    description = "The unique ID of the user to delete",
                    required = true,
                    example = "1"
            )
            @PathVariable(name = "id") Long id
    ) {
        userService.deleteUser(id);
    }

    @PostMapping("/{id}/change-password")
    @Operation(
            summary = "Change user password",
            description = "Changes the password of an existing user. Returns 204 if successful, or 404 if the user does not exist."
    )
    public void changePassword(
            @Parameter(
                    description = "The unique ID of the user whose password is to be changed",
                    required = true,
                    example = "1"
            )
            @PathVariable(name = "id") Long id,
            @RequestBody ChangePasswordRequest request
    ) {
        userService.changePassword(id, request);
    }

}
