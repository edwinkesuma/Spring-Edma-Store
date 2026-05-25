package com.edwinkesuma.springedmastore.features.wallet.application.dto;

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

        String description
) {
}
