package com.dieegopa.store.controllers;

import com.dieegopa.store.dtos.CartDto;
import com.dieegopa.store.entities.Cart;
import com.dieegopa.store.mappers.CartMapper;
import com.dieegopa.store.repositories.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    @PostMapping
    public ResponseEntity<CartDto> createCart(

    ) {
        var cart = new Cart();
        cartRepository.save(cart);

        var cartDto = cartMapper.toCartDto(cart);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartDto);
    }
}
