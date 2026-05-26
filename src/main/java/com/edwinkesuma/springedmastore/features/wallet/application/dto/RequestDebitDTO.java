package com.edwinkesuma.springedmastore.features.wallet.application.dto;

import com.edwinkesuma.springedmastore.features.wallet.domain.enums.WalletReferenceType;
import com.edwinkesuma.springedmastore.features.wallet.domain.enums.WalletTransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record RequestDebitDTO(
        @NotNull
        UUID userId,

        @NotNull
        @Positive
        BigDecimal amount,

        @NotNull
        WalletTransactionType transactionType,

        String description,

        WalletReferenceType referenceType,

        UUID referenceId
) {
}
