package com.edwinkesuma.springedmastore.features.user.application.dto;

import jakarta.validation.constraints.NotBlank;

public record RequestRefreshTokenDTO(
        @NotBlank
        String refreshToken
) {
}
