package com.dieegopa.store.services;

import com.dieegopa.store.entities.Order;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
}
