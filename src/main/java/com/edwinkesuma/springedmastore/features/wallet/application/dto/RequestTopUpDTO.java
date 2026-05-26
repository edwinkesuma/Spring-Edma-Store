package com.edwinkesuma.springedmastore.features.wallet.application.dto;

import com.edwinkesuma.springedmastore.features.wallet.domain.enums.WalletReferenceType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record RequestTopUpDTO(
        @NotNull
        UUID userId,

        @NotNull
        @Positive
        BigDecimal amount,

        String description,

        WalletReferenceType referenceType,

        UUID referenceId
) {
}
