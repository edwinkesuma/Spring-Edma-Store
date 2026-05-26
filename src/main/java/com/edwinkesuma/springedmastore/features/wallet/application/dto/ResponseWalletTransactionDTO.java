package com.edwinkesuma.springedmastore.features.wallet.application.dto;

import com.edwinkesuma.springedmastore.features.wallet.domain.enums.TransactionDirection;
import com.edwinkesuma.springedmastore.features.wallet.domain.enums.WalletTransactionStatus;
import com.edwinkesuma.springedmastore.features.wallet.domain.enums.WalletTransactionType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record ResponseWalletTransactionDTO(
        UUID transactionId,

        String transactionCode,

        WalletTransactionType type,

        WalletTransactionStatus status,

        TransactionDirection direction,

        BigDecimal amount,

        BigDecimal balanceBefore,

        BigDecimal balanceAfter,

        String currency,

        String description,

        Instant createdAt
) {
}
