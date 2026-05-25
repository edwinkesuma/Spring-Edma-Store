package com.edwinkesuma.springedmastore.features.wallet.domain.entity;

import com.edwinkesuma.springedmastore.common.entity.BaseEntity;
import com.edwinkesuma.springedmastore.features.wallet.domain.enums.TransactionDirection;
import com.edwinkesuma.springedmastore.features.wallet.domain.enums.WalletTransactionStatus;
import com.edwinkesuma.springedmastore.features.wallet.domain.enums.WalletTransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletTransaction extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @NotBlank
    @Size(max = 100)
    @Column(
            name = "transaction_code",
            nullable = false,
            unique = true,
            length = 100
    )
    private String transactionCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private WalletTransactionType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private WalletTransactionStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TransactionDirection direction;

    @NotNull
    @DecimalMin(value = "0.01")
    @Digits(integer = 17, fraction = 2)
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Size(max = 30)
    @Column(name = "reference_type", length = 30)
    private String referenceType;

    @Column(name = "reference_id")
    private UUID referenceId;

    @Size(max = 100)
    @Column(name = "external_reference", length = 100)
    private String externalReference;

    @Size(max = 1000)
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
        this.status = WalletTransactionStatus.SUCCESS;
    }

    public void markFailed() {
        this.status = WalletTransactionStatus.FAILED;
    }

    public void markCancelled() {
        this.status = WalletTransactionStatus.CANCELLED;
    }
}