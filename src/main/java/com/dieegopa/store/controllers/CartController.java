package com.dieegopa.store.controllers;

import com.dieegopa.store.dtos.AddItemToCartRequest;
import com.dieegopa.store.dtos.CartDto;
import com.dieegopa.store.dtos.CartItemDto;
import com.dieegopa.store.entities.Cart;
import com.dieegopa.store.entities.CartItem;
import com.dieegopa.store.mappers.CartMapper;
import com.dieegopa.store.repositories.CartRepository;
import com.dieegopa.store.repositories.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<CartDto> createCart() {
        var cart = new Cart();
        cartRepository.save(cart);

        var cartDto = cartMapper.toCartDto(cart);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(
            @PathVariable(value = "cartId") String cartId,
            @Valid @RequestBody AddItemToCartRequest request
    ) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }

        var product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().build();
        }

        var cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cart.getItems().add(cartItem);
        }

        cartRepository.save(cart);

        var cartItemDto = cartMapper.toCartItemDto(cartItem);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);

    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(
            @PathVariable(value = "cartId") String cartId
    ) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cartMapper.toCartDto(cart));
    }
}
