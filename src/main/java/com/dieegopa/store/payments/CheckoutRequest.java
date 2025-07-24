package com.dieegopa.store.payments;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CheckoutRequest {
    @NotNull(message = "Cart ID cannot be null")
    private String cartId;
}