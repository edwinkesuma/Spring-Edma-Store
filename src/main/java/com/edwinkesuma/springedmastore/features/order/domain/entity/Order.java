package com.edwinkesuma.springedmastore.features.order.domain.entity;

import com.edwinkesuma.springedmastore.common.entity.BaseEntity;
import com.edwinkesuma.springedmastore.features.order.domain.enums.OrderStatus;
import com.edwinkesuma.springedmastore.features.user.domain.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Email
    @Size(max = 150)
    private String guestEmail;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, unique = true)
    private String orderCode;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String targetId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status;

    @NotNull
    @DecimalMin(value = "0.0")
    @Digits(integer = 17, fraction = 2)
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    private Instant paidAt;
}
