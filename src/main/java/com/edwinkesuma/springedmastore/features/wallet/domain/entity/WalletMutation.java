package com.edwinkesuma.springedmastore.features.wallet.domain.entity;

import com.edwinkesuma.springedmastore.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "wallet_mutations",
        indexes = {
                @Index(
                        name = "idx_wallet_mutations_wallet_id",
                        columnList = "wallet_id"
                ),
                @Index(
                        name = "idx_wallet_mutations_transaction_id",
                        columnList = "transaction_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletMutation extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_id", nullable = false)
    private WalletTransaction transaction;

    @NotNull
    @Digits(integer = 17, fraction = 2)
    @DecimalMin(value = "0.00")
    @Column(name = "balance_before", nullable = false, precision = 19, scale = 2)
    private BigDecimal balanceBefore;

    @NotNull
    @Digits(integer = 17, fraction = 2)
    @DecimalMin(value = "0.01")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @NotNull
    @Digits(integer = 17, fraction = 2)
    @DecimalMin(value = "0.00")
    @Column(name = "balance_after", nullable = false, precision = 19, scale = 2)
    private BigDecimal balanceAfter;
}
