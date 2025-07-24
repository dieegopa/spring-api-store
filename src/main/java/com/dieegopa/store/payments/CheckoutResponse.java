package com.dieegopa.store.payments;

import lombok.Data;

@Data
public class CheckoutResponse {
    private Long orderId;
    private String checkoutUrl;

    public CheckoutResponse(Long id, String checkoutUrl) {
        this.orderId = id;
        this.checkoutUrl = checkoutUrl;
    }
}