package com.dieegopa.store.controllers;

import com.dieegopa.store.dtos.AddItemToCartRequest;
import com.dieegopa.store.dtos.CartDto;
import com.dieegopa.store.dtos.CartItemDto;
import com.dieegopa.store.dtos.UpdateCartItemRequest;
import com.dieegopa.store.services.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart() {

        var cartDto = cartService.createCart();

        return ResponseEntity.status(HttpStatus.CREATED).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(
            @PathVariable(value = "cartId") String cartId,
            @Valid @RequestBody AddItemToCartRequest request
    ) {
        var cartItemDto = cartService.addToCart(cartId, request.getProductId());

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public CartDto getCart(
            @PathVariable(value = "cartId") String cartId
    ) {

        return cartService.getCart(cartId);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public CartItemDto updateItem(
            @PathVariable(value = "cartId") String cartId,
            @PathVariable(value = "productId") Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {

        return cartService.updateItem(cartId, productId, request.getQuantity());
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeItem(
            @PathVariable(name = "cartId") String cartId,
            @PathVariable(name = "productId") Long productId
    ) {
        cartService.removeItem(cartId, productId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(
            @PathVariable(name = "cartId") String cartId
    ) {
        cartService.clearCart(cartId);

        return ResponseEntity.noContent().build();
    }
}
