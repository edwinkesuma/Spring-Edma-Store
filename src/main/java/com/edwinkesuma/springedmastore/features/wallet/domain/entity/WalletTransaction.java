package com.edwinkesuma.springedmastore.features.wallet.domain.entity;

import com.edwinkesuma.springedmastore.common.entity.BaseEntity;
import com.edwinkesuma.springedmastore.features.wallet.domain.enums.TransactionDirection;
import com.edwinkesuma.springedmastore.features.wallet.domain.enums.WalletReferenceType;
import com.edwinkesuma.springedmastore.features.wallet.domain.enums.WalletTransactionStatus;
import com.edwinkesuma.springedmastore.features.wallet.domain.enums.WalletTransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(
        name = "wallet_transactions",
        indexes = {
                @Index(
                        name = "idx_wallet_transactions_wallet_id",
                        columnList = "wallet_id"
                ),
                @Index(
                        name = "idx_wallet_transactions_reference",
                        columnList = "reference_type, reference_id"
                ),
                @Index(
                        name = "idx_wallet_transactions_external_reference",
                        columnList = "external_reference"
                )
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_wallet_transaction_code",
                        columnNames = "transaction_code"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class WalletTransaction extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(
            name = "transaction_code",
            nullable = false,
            unique = true,
            length = 100
    )
    private String transactionCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private WalletTransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private WalletTransactionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TransactionDirection direction;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "reference_type", length = 30)
    @Enumerated(EnumType.STRING)
    private WalletReferenceType referenceType;


    @Column(name = "reference_id")
    private UUID referenceId;

    @Column(name = "external_reference", length = 100)
    private String externalReference;

    @Column(columnDefinition = "TEXT")
    private String description;

    /*
     * =========================
     * DOMAIN METHODS
     * =========================
     */

    public boolean isSuccess() {
        return this.status == WalletTransactionStatus.SUCCESS;
    }

    public boolean isPending() {
        return this.status == WalletTransactionStatus.PENDING;
    }

    public boolean isFailed() {
        return this.status == WalletTransactionStatus.FAILED;
    }

    public void markSuccess() {
        validatePendingState();

        this.status = WalletTransactionStatus.SUCCESS;
    }

    public void markFailed() {
        validatePendingState();

        this.status = WalletTransactionStatus.FAILED;
    }

    public void markCancelled() {
        validatePendingState();

        this.status = WalletTransactionStatus.CANCELLED;
    }

    private void validatePendingState() {
        if (this.status != WalletTransactionStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending transaction can be updated"
            );
        }
    }
}