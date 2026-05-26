package com.edwinkesuma.springedmastore.features.user.application.usecase;

import com.edwinkesuma.springedmastore.common.exception.UserAlreadyExistsException;
import com.edwinkesuma.springedmastore.features.user.application.dto.RequestRegisterDTO;
import com.edwinkesuma.springedmastore.features.user.application.dto.ResponseRegisterDTO;
import com.edwinkesuma.springedmastore.features.user.application.dto.UserDTO;
import com.edwinkesuma.springedmastore.features.user.domain.entity.User;
import com.edwinkesuma.springedmastore.features.user.domain.enums.UserRole;
import com.edwinkesuma.springedmastore.features.user.domain.repository.UserRepository;
import com.edwinkesuma.springedmastore.features.wallet.domain.entity.Wallet;
import com.edwinkesuma.springedmastore.features.wallet.domain.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ResponseRegisterDTO execute(RequestRegisterDTO request) {

        User
                user =
                User.builder()
                        .username(request.username())
                        .email(request.email())
                        .passwordHash(passwordEncoder.encode(request.password()))
                        .phone(request.phone())
                        .isActive(true)
                        .role(UserRole.CUSTOMER)
                        .build();

        // Save user
        User savedUser;
        try {
            savedUser = userRepository.save(user);

            // ADD WALLET
            Wallet wallet = Wallet.create(savedUser);
            walletRepository.save(wallet);
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
