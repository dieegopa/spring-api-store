package com.dieegopa.store.controllers;

import com.dieegopa.store.dtos.CheckoutRequest;
import com.dieegopa.store.dtos.CheckoutResponse;
import com.dieegopa.store.services.CheckoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
@Tag(name = "Checkout", description = "Operations related to the checkout process")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping
    @Operation(
            summary = "Checkout",
            description = "Processes the checkout for a given order. Requires a valid CheckoutRequest."
    )
    public CheckoutResponse checkout(@Valid @RequestBody CheckoutRequest request) {
        return checkoutService.checkout(request);
    }

}
