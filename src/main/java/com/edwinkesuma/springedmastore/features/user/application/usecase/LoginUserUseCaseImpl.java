package com.edwinkesuma.springedmastore.features.user.application.usecase;

import com.edwinkesuma.springedmastore.common.exception.InvalidCredentialsException;
import com.edwinkesuma.springedmastore.features.user.application.dto.RequestLoginDTO;
import com.edwinkesuma.springedmastore.features.user.application.dto.ResponseLoginDTO;
import com.edwinkesuma.springedmastore.features.user.application.dto.UserDTO;
import com.edwinkesuma.springedmastore.features.user.domain.entity.User;
import com.edwinkesuma.springedmastore.features.user.domain.repository.UserRepository;
import com.edwinkesuma.springedmastore.infrastructure.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserUseCaseImpl implements LoginUserUseCase {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public ResponseLoginDTO execute(RequestLoginDTO request) {
        try {

            var resultAuth =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.email(),
                                    request.password()
                            )
                    );

            String jwtToken = jwtUtil.generateJwtToken(resultAuth);

            var loggedInUser = (User) resultAuth.getPrincipal();

            var userDto = new UserDTO(
                    loggedInUser.getId(),
                    loggedInUser.getUsername(),
                    loggedInUser.getEmail(),
                    loggedInUser.getPhone(),
                    loggedInUser.getRole().name(),
                    loggedInUser.isActive(),
                    loggedInUser.getCreatedAt()
            );

            return new ResponseLoginDTO(
                    HttpStatus.OK.getReasonPhrase(),
                    userDto,
                    jwtToken
            );

        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }
}
