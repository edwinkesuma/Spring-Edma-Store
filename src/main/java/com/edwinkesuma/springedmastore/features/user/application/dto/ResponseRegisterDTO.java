package com.edwinkesuma.springedmastore.features.user.application.dto;

public record ResponseRegisterDTO(
        String message,
        UserDTO user
) {
}
