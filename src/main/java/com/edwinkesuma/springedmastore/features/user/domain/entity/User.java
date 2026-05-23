package com.edwinkesuma.springedmastore.features.user.domain.entity;

import com.edwinkesuma.springedmastore.common.entity.BaseEntity;
import com.edwinkesuma.springedmastore.features.user.domain.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity {

    @Size(max = 100)
    @Column(unique = true, length = 100)
    private String username;

    @NotBlank
    @Email
    @Size(max = 150)
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank
    @Size(min = 8, max = 255)
    @Column(nullable = false)
    private String passwordHash;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Pattern(
            regexp = "^[0-9+\\-() ]*$",
            message = "Invalid phone number format"
    )
    @Size(max = 30)
    @Column(length = 30)
    private String phone;

    @NotNull
    @Column(nullable = false)
    private Boolean isActive = true;
}