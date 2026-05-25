package com.edwinkesuma.springedmastore.features.wallet.domain.entity;

import com.edwinkesuma.springedmastore.common.entity.BaseEntity;
import com.edwinkesuma.springedmastore.features.user.domain.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "wallets",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_wallet_user",
                        columnNames = "user_id"
                )
        }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet extends BaseEntity {

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private User user;

    @NotBlank
    @Size(max = 10)
    @Column(nullable = false, length = 10)
    @Builder.Default
    private String currency = "IDR";

    @NotNull
    @DecimalMin(value = "0.00")
    @Digits(integer = 17, fraction = 2)
    @Column(nullable = false, precision = 19, scale = 2)
    @Setter(AccessLevel.NONE)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    @NotNull
    @Column(name = "is_frozen", nullable = false)
    @Builder.Default
    private Boolean isFrozen = false;

    @Version
    private Long version;

    /*
     * =========================
     * DOMAIN METHODS
     * =========================
     */

    public void credit(BigDecimal amount) {
        validateWalletUsable();
        validateAmount(amount);

        this.balance = this.balance.add(amount);
    }

    public void debit(BigDecimal amount) {
        validateWalletUsable();
        validateAmount(amount);

        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient wallet balance");
        }

        this.balance = this.balance.subtract(amount);
    }

    public void freeze() {
        this.isFrozen = true;
    }

    public void unfreeze() {
        this.isFrozen = false;
    }

    public boolean hasSufficientBalance(BigDecimal amount) {
        validateAmount(amount);

        return this.balance.compareTo(amount) >= 0;
    }

    /*
     * =========================
     * VALIDATIONS
     * =========================
     */

    private void validateWalletUsable() {
        if (Boolean.TRUE.equals(this.isFrozen)) {
            throw new IllegalStateException("Wallet is frozen");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }

        if (amount.scale() > 2) {
            throw new IllegalArgumentException("Amount cannot have more than 2 decimal places");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }
}