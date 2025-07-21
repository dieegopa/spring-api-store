package com.dieegopa.store.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CheckoutRequest {
    @NotNull(message = "Cart ID cannot be null")
    private String cartId;
}