package com.edwinkesuma.springedmastore.features.user.application.usecase;

import com.edwinkesuma.springedmastore.common.exception.UserAlreadyExistsException;
import com.edwinkesuma.springedmastore.features.user.application.dto.RequestRegisterDTO;
import com.edwinkesuma.springedmastore.features.user.application.dto.ResponseRegisterDTO;
import com.edwinkesuma.springedmastore.features.user.application.dto.UserDTO;
import com.edwinkesuma.springedmastore.features.user.domain.entity.User;
import com.edwinkesuma.springedmastore.features.user.domain.enums.UserRole;
import com.edwinkesuma.springedmastore.features.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseRegisterDTO execute(RequestRegisterDTO request) {

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
        User savedUser;
        try {
            savedUser = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        var userDto = new UserDTO(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                savedUser.getRole().name(),
                savedUser.isActive(),
                savedUser.getCreatedAt()
        );

        return new ResponseRegisterDTO(
                HttpStatus.CREATED.getReasonPhrase(),
                userDto
        );
    }
}
