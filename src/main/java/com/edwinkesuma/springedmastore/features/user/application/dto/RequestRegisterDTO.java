package com.edwinkesuma.springedmastore.features.user.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RequestRegisterDTO(

        @NotBlank
        @Size(min = 5, max = 30, message = "The length of the username should be between 5 and 30 characters")
        String username,

        @NotBlank
        @Email
        @Size(max = 150)
        String email,

        @NotBlank
        @Size(min = 8, max = 255)
        String password,

        @Pattern(
                regexp = "^\\+?[0-9]{8,15}$",
                message = "Invalid phone number format"
        )
        @Size(max = 30)
        String phone
) {
}
