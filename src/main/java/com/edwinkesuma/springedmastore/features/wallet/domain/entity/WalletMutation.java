package com.edwinkesuma.springedmastore.features.wallet.domain.entity;

import com.edwinkesuma.springedmastore.common.entity.ImmutableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletMutation extends ImmutableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_id", nullable = false)
    private WalletTransaction transaction;

    @Column(
            name = "balance_before",
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal balanceBefore;

    @Column(
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal amount;

    @Column(
            name = "balance_after",
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal balanceAfter;
}