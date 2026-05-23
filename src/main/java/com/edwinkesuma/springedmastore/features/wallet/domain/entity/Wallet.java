package com.edwinkesuma.springedmastore.features.wallet.domain.entity;

import com.edwinkesuma.springedmastore.common.entity.BaseEntity;
import com.edwinkesuma.springedmastore.features.user.domain.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
@Getter
@Setter
public class Wallet extends BaseEntity {

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @NotBlank
    @Size(max = 10)
    @Column(nullable = false, length = 10)
    private String currency = "IDR";

    @NotNull
    @DecimalMin(value = "0.0")
    @Digits(integer = 17, fraction = 2)
    @Column(nullable = false, precision = 19, scale = 2)
    @Setter(AccessLevel.NONE)
    private BigDecimal balance = BigDecimal.ZERO;

    @Version
    private Long version;

    public void credit(BigDecimal amount) {
        validateAmount(amount);
        this.balance = this.balance.add(amount);
    }

    public void debit(BigDecimal amount) {
        validateAmount(amount);

        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient wallet balance");
        }

        this.balance = this.balance.subtract(amount);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }
}