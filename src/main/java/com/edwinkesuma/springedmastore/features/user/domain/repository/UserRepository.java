package com.edwinkesuma.springedmastore.features.user.domain.repository;

import com.edwinkesuma.springedmastore.features.user.domain.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(@NotBlank @Email @Size(max = 150) String email);

    boolean existsByUsername(@NotBlank @Size(min = 5, max = 30, message = "The length of the username should be between 5 and 30 characters") String username);
}
