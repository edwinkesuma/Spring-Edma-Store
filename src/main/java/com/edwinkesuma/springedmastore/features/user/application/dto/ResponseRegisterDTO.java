package com.edwinkesuma.springedmastore.features.user.application.dto;

import java.time.Instant;
import java.util.UUID;

public record ResponseRegisterDTO(
        UUID id,
        String username,
        String email,
        String phone,
        String role,
        boolean isActive,
        String message,
        Instant createdAt
) {
}
