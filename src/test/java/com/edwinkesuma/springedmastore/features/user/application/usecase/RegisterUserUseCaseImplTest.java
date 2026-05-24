package com.edwinkesuma.springedmastore.features.user.application.usecase;

import com.edwinkesuma.springedmastore.common.exception.UserAlreadyExistsException;
import com.edwinkesuma.springedmastore.features.user.application.dto.RequestRegisterDTO;
import com.edwinkesuma.springedmastore.features.user.application.dto.ResponseRegisterDTO;
import com.edwinkesuma.springedmastore.features.user.domain.entity.User;
import com.edwinkesuma.springedmastore.features.user.domain.enums.UserRole;
import com.edwinkesuma.springedmastore.features.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterUserUseCaseImpl registerUserUseCase;

    @Test
    void shouldRegisterUserSuccessfully() {

        RequestRegisterDTO request = new RequestRegisterDTO(
                "edwin123",
                "edwin@gmail.com",
                "08123456789",
                "password123"
        );

        when(userRepository.existsByEmail(request.email()))
                .thenReturn(false);

        when(userRepository.existsByUsername(request.username()))
                .thenReturn(false);

        when(passwordEncoder.encode(request.password()))
                .thenReturn("hashed_password");

        User savedUser = User.builder()
                .username(request.username())
                .email(request.email())
                .phone(request.phone())
                .role(UserRole.CUSTOMER)
                .isActive(true)
                .build();

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        ResponseRegisterDTO response =
                registerUserUseCase.execute(request);

        assertNotNull(response);

        assertEquals("edwin123", response.username());
        assertEquals("edwin@gmail.com", response.email());
        assertEquals("CUSTOMER", response.role());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        RequestRegisterDTO request = new RequestRegisterDTO(
                "edwin123",
                "edwin@gmail.com",
                "08123456789",
                "password123"
        );

        when(userRepository.existsByEmail(request.email()))
                .thenReturn(true);

        assertThrows(
                UserAlreadyExistsException.class,
                () -> registerUserUseCase.execute(request)
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {

        RequestRegisterDTO request = new RequestRegisterDTO(
                "edwin123",
                "edwin@gmail.com",
                "08123456789",
                "password123"
        );

        when(userRepository.existsByEmail(request.email()))
                .thenReturn(false);

        when(userRepository.existsByUsername(request.username()))
                .thenReturn(true);

        assertThrows(
                UserAlreadyExistsException.class,
                () -> registerUserUseCase.execute(request)
        );

        verify(userRepository, never()).save(any(User.class));
    }
}
