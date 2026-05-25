package com.edwinkesuma.springedmastore.features.user.application.usecase;

import com.edwinkesuma.springedmastore.common.exception.InvalidCredentialsException;
import com.edwinkesuma.springedmastore.features.user.application.dto.RequestLoginDTO;
import com.edwinkesuma.springedmastore.features.user.application.dto.ResponseLoginDTO;
import com.edwinkesuma.springedmastore.features.user.domain.entity.User;
import com.edwinkesuma.springedmastore.features.user.domain.enums.UserRole;
import com.edwinkesuma.springedmastore.features.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUserUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private LoginUserUseCaseImpl loginUserUseCase;

    @Test
    void shouldLoginSuccessfully() {

        // Arrange
        RequestLoginDTO request = new RequestLoginDTO(
                "edwin@gmail.com",
                "password123"
        );

        User user = new User();
        user.setUsername("edwin123");
        user.setEmail("edwin@gmail.com");
        user.setPhone("08123456789");
        user.setRole(UserRole.CUSTOMER);
        user.setActive(true);

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(
                UsernamePasswordAuthenticationToken.class
        ))).thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(user);

        // Act
        ResponseLoginDTO result = loginUserUseCase.execute(request);

        // Assert
        assertNotNull(result);

        assertEquals("OK", result.message());

        assertEquals(user.getId(), result.user().id());
        assertEquals(user.getUsername(), result.user().username());
        assertEquals(user.getEmail(), result.user().email());
        assertEquals(user.getPhone(), result.user().phone());
        assertEquals(user.getRole().name(), result.user().role());
        assertEquals(user.isActive(), result.user().isActive());

        verify(authenticationManager, times(1))
                .authenticate(any(
                        UsernamePasswordAuthenticationToken.class
                ));
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionWhenAuthenticationFails() {

        // Arrange
        RequestLoginDTO request = new RequestLoginDTO(
                "wrong@gmail.com",
                "wrongpassword"
        );

        when(authenticationManager.authenticate(any(
                UsernamePasswordAuthenticationToken.class
        ))).thenThrow(new BadCredentialsException("Bad credentials"));

        // Act & Assert
        InvalidCredentialsException exception =
                assertThrows(
                        InvalidCredentialsException.class,
                        () -> loginUserUseCase.execute(request)
                );

        assertEquals(
                "Invalid email or password",
                exception.getMessage()
        );

        verify(authenticationManager, times(1))
                .authenticate(any(
                        UsernamePasswordAuthenticationToken.class
                ));
    }
}