package com.edwinkesuma.springedmastore.features.wallet.application.usecase.impl;

import com.edwinkesuma.springedmastore.common.exception.WalletNotFoundException;
import com.edwinkesuma.springedmastore.features.wallet.domain.entity.Wallet;
import com.edwinkesuma.springedmastore.features.wallet.domain.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetBalanceUseCaseImplTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private GetBalanceUseCaseImpl useCase;

    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
    }

    @Test
    void execute_shouldReturnBalanceSuccessfully() {
        // GIVEN
        BigDecimal balance = BigDecimal.valueOf(250000);

        Wallet wallet =
                Wallet.builder()
                        .balance(balance)
                        .build();

        when(walletRepository.findByUserId(userId))
                .thenReturn(Optional.of(wallet));

        // WHEN
        BigDecimal result = useCase.execute(userId);

        // THEN
        assertThat(result)
                .isEqualByComparingTo(balance);

        verify(walletRepository).findByUserId(userId);
    }

    @Test
    void execute_shouldThrowWalletNotFoundException_whenWalletNotFound() {
        // GIVEN
        when(walletRepository.findByUserId(userId))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        assertThatThrownBy(() -> useCase.execute(userId))
                .isInstanceOf(WalletNotFoundException.class);

        verify(walletRepository).findByUserId(userId);
    }

    @Test
    void execute_shouldReturnZeroBalance() {
        // GIVEN
        BigDecimal balance = BigDecimal.ZERO;

        Wallet wallet =
                Wallet.builder()
                        .balance(balance)
                        .build();

        when(walletRepository.findByUserId(userId))
                .thenReturn(Optional.of(wallet));

        // WHEN
        BigDecimal result = useCase.execute(userId);

        // THEN
        assertThat(result)
                .isEqualByComparingTo(BigDecimal.ZERO);
    }
}
