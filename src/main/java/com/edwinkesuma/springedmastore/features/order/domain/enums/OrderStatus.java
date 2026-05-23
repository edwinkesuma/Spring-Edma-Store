package com.edwinkesuma.springedmastore.features.order.domain.enums;

public enum OrderStatus {
    CREATED,
    WAITING_PAYMENT,
    PAID,
    PROCESSING,
    SUCCESS,
    FAILED,
    CANCELLED,
    REFUNDED,
    EXPIRED
}
