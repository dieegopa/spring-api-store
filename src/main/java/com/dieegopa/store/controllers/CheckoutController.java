package com.dieegopa.store.controllers;

import com.dieegopa.store.dtos.CheckoutRequest;
import com.dieegopa.store.dtos.CheckoutResponse;
import com.dieegopa.store.entities.OrderStatus;
import com.dieegopa.store.repositories.OrderRepository;
import com.dieegopa.store.services.CheckoutService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/checkout")
@Tag(name = "Checkout", description = "Operations related to the checkout process")
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final OrderRepository orderRepository;

    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;

    @PostMapping
    @Operation(
            summary = "Checkout",
            description = "Processes the checkout for a given order. Requires a valid CheckoutRequest."
    )
    public CheckoutResponse checkout(@Valid @RequestBody CheckoutRequest request) {
        return checkoutService.checkout(request);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(
            @RequestHeader("Stripe-Signature") String signature,
            @RequestBody String payload
    ) {
        try {
            var event = Webhook.constructEvent(payload, signature, webhookSecretKey);
            System.out.println(event.getType());

            var stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);

            switch (event.getType()) {
                case "payment_intent.succeeded" -> {
                    var paymentIntent = (PaymentIntent) stripeObject;
                    if(paymentIntent != null) {
                        var orderId = paymentIntent.getMetadata().get("order_id");
                        var order = orderRepository.findById(Long.valueOf(orderId)).orElseThrow();
                        order.setStatus(OrderStatus.PAID);
                        orderRepository.save(order);
                    }
                }
                case "payment_intent.payment_failed" -> {
                    // update order status to FAILED
                }
            }

            return ResponseEntity.ok().build();


        } catch (SignatureVerificationException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

}
