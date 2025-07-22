package com.dieegopa.store.services;

import com.dieegopa.store.dtos.CheckoutRequest;
import com.dieegopa.store.dtos.CheckoutResponse;
import com.dieegopa.store.entities.Order;
import com.dieegopa.store.exceptions.CartEmptyException;
import com.dieegopa.store.exceptions.CartNotFoundException;
import com.dieegopa.store.repositories.CartRepository;
import com.dieegopa.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CheckoutService {

    private final CartService cartService;
    private final AuthService authService;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    public CheckoutResponse checkout(CheckoutRequest request) {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        if (cart.isEmpty()) {
            throw new CartEmptyException();
        }

        var order = Order.fromCart(cart, authService.getCurrentUser());
        orderRepository.save(order);
        cartService.clearCart(cart.getId());

        return new CheckoutResponse(order.getId());
    }

}
