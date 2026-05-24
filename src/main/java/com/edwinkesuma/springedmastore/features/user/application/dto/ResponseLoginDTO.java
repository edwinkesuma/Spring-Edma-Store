package com.edwinkesuma.springedmastore.features.user.application.dto;

public record ResponseLoginDTO(
        String message,
        UserDTO user
) {
}
