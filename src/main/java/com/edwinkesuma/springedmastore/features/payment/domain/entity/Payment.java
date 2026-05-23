package com.edwinkesuma.springedmastore.features.payment.domain.entity;

import com.edwinkesuma.springedmastore.common.entity.BaseEntity;
import com.edwinkesuma.springedmastore.features.order.domain.entity.Order;
import com.edwinkesuma.springedmastore.features.payment.domain.enums.PaymentGateway;
import com.edwinkesuma.springedmastore.features.payment.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment extends BaseEntity {

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentGateway gateway;

    @Size(max = 50)
    @Column(length = 50)
    private String channel;

    @Size(max = 100)
    private String gatewayReference;

    @NotNull
    @DecimalMin(value = "0.0")
    @Digits(integer = 17, fraction = 2)
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal grossAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Column(columnDefinition = "jsonb")
    private String rawResponse;
}