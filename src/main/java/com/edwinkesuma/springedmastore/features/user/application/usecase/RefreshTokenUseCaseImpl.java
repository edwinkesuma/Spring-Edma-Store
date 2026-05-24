package com.edwinkesuma.springedmastore.features.user.application.usecase;

import com.edwinkesuma.springedmastore.common.exception.InvalidTokenException;
import com.edwinkesuma.springedmastore.features.user.domain.entity.User;
import com.edwinkesuma.springedmastore.features.user.domain.repository.UserRepository;
import com.edwinkesuma.springedmastore.infrastructure.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RefreshTokenUseCaseImpl implements RefreshTokenUseCase {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public String execute(String refreshToken) {
        if (!jwtUtil.isTokenValid(refreshToken)) {
            throw new InvalidTokenException("Invalid refresh token");
        }

        if (!jwtUtil.isRefreshToken(refreshToken)) {
            throw new InvalidTokenException("Invalid refresh token");
        }

        String email = jwtUtil.extractUsername(refreshToken);

        List<String> roles = jwtUtil.extractRoles(refreshToken);

        User
                user =
                userRepository.findUserByEmail(email)
                        .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));

        return jwtUtil.generateAccessToken(user, roles);
    }
}
