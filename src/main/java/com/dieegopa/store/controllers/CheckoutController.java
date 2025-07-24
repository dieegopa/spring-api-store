package com.dieegopa.store.controllers;

import com.dieegopa.store.dtos.CheckoutRequest;
import com.dieegopa.store.dtos.CheckoutResponse;
import com.dieegopa.store.services.CheckoutService;
import com.dieegopa.store.services.WebhookRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
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

    @PostMapping("/webhook")
    public void handleWebhook(
            @RequestHeader Map<String, String> headers,
            @RequestBody String payload
    ) {
        checkoutService.handleWebhookEvent(new WebhookRequest(headers, payload));
    }

}
