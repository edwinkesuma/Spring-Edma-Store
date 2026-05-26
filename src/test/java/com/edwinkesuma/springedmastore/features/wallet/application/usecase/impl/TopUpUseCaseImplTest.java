package com.edwinkesuma.springedmastore.features.wallet.application.usecase.impl;

import com.edwinkesuma.springedmastore.common.exception.WalletNotFoundException;
import com.edwinkesuma.springedmastore.features.wallet.application.dto.RequestTopUpDTO;
import com.edwinkesuma.springedmastore.features.wallet.application.dto.ResponseWalletTransactionDTO;
import com.edwinkesuma.springedmastore.features.wallet.application.factory.WalletMutationFactory;
import com.edwinkesuma.springedmastore.features.wallet.application.factory.WalletTransactionFactory;
import com.edwinkesuma.springedmastore.features.wallet.application.mapper.WalletTransactionMapper;
import com.edwinkesuma.springedmastore.features.wallet.application.service.WalletHelper;
import com.edwinkesuma.springedmastore.features.wallet.domain.entity.Wallet;
import com.edwinkesuma.springedmastore.features.wallet.domain.entity.WalletMutation;
import com.edwinkesuma.springedmastore.features.wallet.domain.entity.WalletTransaction;
import com.edwinkesuma.springedmastore.features.wallet.domain.enums.WalletReferenceType;
import com.edwinkesuma.springedmastore.features.wallet.domain.repository.WalletMutationRepository;
import com.edwinkesuma.springedmastore.features.wallet.domain.repository.WalletRepository;
import com.edwinkesuma.springedmastore.features.wallet.domain.repository.WalletTransactionRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopUpUseCaseImplTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private WalletTransactionRepository walletTransactionRepository;

    @Mock
    private WalletMutationRepository walletMutationRepository;

    @Mock
    private WalletHelper walletHelper;

    @Mock
    private WalletTransactionFactory walletTransactionFactory;

    @Mock
    private WalletMutationFactory walletMutationFactory;

    @Mock
    private WalletTransactionMapper walletTransactionMapper;

    @InjectMocks
    private TopUpUseCaseImpl useCase;

    private UUID userId;
    private UUID referenceId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        referenceId = UUID.randomUUID();
    }

    @Test
    void execute_shouldTopUpSuccessfully() {
        // GIVEN
        BigDecimal amount = BigDecimal.valueOf(100000);

        RequestTopUpDTO request =
                new RequestTopUpDTO(
                        userId,
                        amount,
                        "Top up saldo",
                        WalletReferenceType.PAYMENT_GATEWAY_TRANSACTION,
                        referenceId
                );

        Wallet wallet =
                Wallet.builder()
                        .balance(BigDecimal.valueOf(50000))
                        .build();

        WalletTransaction transaction =
                WalletTransaction.builder()
                        .wallet(wallet)
                        .amount(amount)
                        .build();

        WalletMutation mutation =
                WalletMutation.builder()
                        .build();

        ResponseWalletTransactionDTO response =
                ResponseWalletTransactionDTO.builder()
                        .amount(amount)
                        .build();

        when(walletRepository.findByUserIdForUpdate(userId))
                .thenReturn(Optional.of(wallet));

        when(walletTransactionFactory.createCredit(wallet, request))
                .thenReturn(transaction);

        when(walletMutationFactory.create(
                eq(wallet),
                eq(transaction),
                eq(BigDecimal.valueOf(50000)),
                eq(amount),
                eq(BigDecimal.valueOf(150000))
        )).thenReturn(mutation);

        when(walletTransactionMapper.toDTO(transaction))
                .thenReturn(response);

        // WHEN
        ResponseWalletTransactionDTO result = useCase.execute(request);

        // THEN
        assertThat(result).isEqualTo(response);

        assertThat(wallet.getBalance())
                .isEqualByComparingTo(BigDecimal.valueOf(150000));

        verify(walletHelper).validateAmount(amount);

        verify(walletTransactionRepository).save(transaction);
        verify(walletMutationRepository).save(mutation);

        verify(walletTransactionMapper).toDTO(transaction);
    }

    @Test
    void execute_shouldThrowEntityNotFoundException_whenWalletNotFound() {
        // GIVEN
        RequestTopUpDTO request =
                new RequestTopUpDTO(
                        userId,
                        BigDecimal.valueOf(100000),
                        "Top up saldo",
                        WalletReferenceType.PAYMENT_GATEWAY_TRANSACTION,
                        referenceId
                );

        when(walletRepository.findByUserIdForUpdate(userId))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        assertThatThrownBy(() -> useCase.execute(request))
                .isInstanceOf(WalletNotFoundException.class);

        verify(walletHelper).validateAmount(request.amount());

        verify(walletTransactionRepository, never())
                .save(any());

        verify(walletMutationRepository, never())
                .save(any());
    }

    @Test
    void execute_shouldUpdateWalletBalanceCorrectly() {
        // GIVEN
        BigDecimal initialBalance = BigDecimal.valueOf(250000);
        BigDecimal topUpAmount = BigDecimal.valueOf(50000);

        RequestTopUpDTO request =
                new RequestTopUpDTO(
                        userId,
                        topUpAmount,
                        "Top up test",
                        WalletReferenceType.PAYMENT_GATEWAY_TRANSACTION,
                        referenceId
                );

        Wallet wallet =
                Wallet.builder()
                        .balance(initialBalance)
                        .build();

        WalletTransaction transaction =
                WalletTransaction.builder()
                        .wallet(wallet)
                        .amount(topUpAmount)
                        .build();

        when(walletRepository.findByUserIdForUpdate(userId))
                .thenReturn(Optional.of(wallet));

        when(walletTransactionFactory.createCredit(wallet, request))
                .thenReturn(transaction);

        // WHEN
        useCase.execute(request);

        // THEN
        assertThat(wallet.getBalance())
                .isEqualByComparingTo(BigDecimal.valueOf(300000));
    }
}
