package com.edwinkesuma.springedmastore.features.user.application.usecase;

import com.edwinkesuma.springedmastore.common.exception.UserAlreadyExistsException;
import com.edwinkesuma.springedmastore.features.user.application.dto.RequestRegisterDTO;

import com.edwinkesuma.springedmastore.features.user.application.dto.ResponseRegisterDTO;
import com.edwinkesuma.springedmastore.features.user.domain.entity.User;
import com.edwinkesuma.springedmastore.features.user.domain.enums.UserRole;
import com.edwinkesuma.springedmastore.features.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseRegisterDTO execute(RequestRegisterDTO request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Email already used");
        }

        if (userRepository.existsByUsername(request.username())) {
            throw new UserAlreadyExistsException("Username already used");
        }

        User
                user =
                User.builder()
                        .username(request.username())
                        .email(request.email())
                        .phone(request.phone())
                        .isActive(true)
                        .role(UserRole.CUSTOMER)
                        .build();

        // Hash user password before saving it
        user.setPasswordHash(passwordEncoder.encode(request.password()));

        // Save user
        User savedUser = userRepository.save(user);

        return new ResponseRegisterDTO(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                savedUser.getRole().name(),
                savedUser.isActive(),
                "User registered successfully",
                savedUser.getCreatedAt()
        );
    }
}
