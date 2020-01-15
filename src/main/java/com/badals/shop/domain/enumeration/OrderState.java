package com.badals.shop.domain.enumeration;

/**
 * The State enumeration.
 */
public enum OrderState {
    AWAITING_PAYMENT, CONTACTED, PAYMENT_AUTHORIZED, PAYMENT_ACCEPTED, IN_PROGRESS, READY, PARTIALLY_DELIVERED, DELIVERED, SHIPPED, CANCELLED
}
