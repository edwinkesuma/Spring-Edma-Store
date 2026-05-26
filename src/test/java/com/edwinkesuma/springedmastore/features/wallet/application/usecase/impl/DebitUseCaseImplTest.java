package com.edwinkesuma.springedmastore.features.wallet.application.usecase.impl;

import com.edwinkesuma.springedmastore.common.exception.InsufficientBalanceException;
import com.edwinkesuma.springedmastore.common.exception.WalletNotFoundException;
import com.edwinkesuma.springedmastore.features.wallet.application.dto.RequestDebitDTO;
import com.edwinkesuma.springedmastore.features.wallet.application.dto.ResponseWalletTransactionDTO;
import com.edwinkesuma.springedmastore.features.wallet.application.factory.WalletMutationFactory;
import com.edwinkesuma.springedmastore.features.wallet.application.factory.WalletTransactionFactory;
import com.edwinkesuma.springedmastore.features.wallet.application.mapper.WalletTransactionMapper;
import com.edwinkesuma.springedmastore.features.wallet.application.service.WalletHelper;
import com.edwinkesuma.springedmastore.features.wallet.domain.entity.Wallet;
import com.edwinkesuma.springedmastore.features.wallet.domain.entity.WalletMutation;
import com.edwinkesuma.springedmastore.features.wallet.domain.entity.WalletTransaction;
import com.edwinkesuma.springedmastore.features.wallet.domain.enums.WalletReferenceType;
import com.edwinkesuma.springedmastore.features.wallet.domain.enums.WalletTransactionType;
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
class DebitUseCaseImplTest {

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
    private DebitUseCaseImpl useCase;

    private UUID userId;
    private UUID referenceId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        referenceId = UUID.randomUUID();
    }

    @Test
    void execute_shouldDebitSuccessfully() {
        // GIVEN
        BigDecimal amount = BigDecimal.valueOf(10000);

        RequestDebitDTO request =
                new RequestDebitDTO(
                        userId,
                        amount,
                        WalletTransactionType.PAYMENT,
                        "Buy product",
                        WalletReferenceType.WALLET_TRANSACTION,
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

        when(walletTransactionFactory.createDebit(wallet, request))
                .thenReturn(transaction);

        when(walletMutationFactory.create(
                eq(wallet),
                eq(transaction),
                eq(BigDecimal.valueOf(50000)),
                eq(amount),
                eq(BigDecimal.valueOf(40000))
        )).thenReturn(mutation);

        when(walletTransactionMapper.toDTO(transaction))
                .thenReturn(response);

        // WHEN
        ResponseWalletTransactionDTO result = useCase.execute(request);

        // THEN
        assertThat(result).isEqualTo(response);

        assertThat(wallet.getBalance())
                .isEqualByComparingTo(BigDecimal.valueOf(40000));

        verify(walletHelper).validateAmount(amount);
        verify(walletHelper).validateSufficientBalance(wallet, amount);

        verify(walletTransactionRepository).save(transaction);
        verify(walletMutationRepository).save(mutation);

        verify(walletTransactionMapper).toDTO(transaction);
    }

    @Test
    void execute_shouldThrowWalletNotFoundException_whenWalletNotFound() {
        // GIVEN
        RequestDebitDTO request =
                new RequestDebitDTO(
                        userId,
                        BigDecimal.valueOf(10000),
                        WalletTransactionType.PAYMENT,
                        "Payment",
                        WalletReferenceType.WALLET_TRANSACTION,
                        referenceId
                );

        when(walletRepository.findByUserIdForUpdate(userId))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        assertThatThrownBy(() -> useCase.execute(request))
                .isInstanceOf(WalletNotFoundException.class);

        verify(walletHelper).validateAmount(request.amount());

        verify(walletHelper, never())
                .validateSufficientBalance(any(), any());

        verify(walletTransactionRepository, never())
                .save(any());

        verify(walletMutationRepository, never())
                .save(any());
    }

    @Test
    void execute_shouldThrowException_whenBalanceInsufficient() {
        // GIVEN
        BigDecimal amount = BigDecimal.valueOf(100000);

        RequestDebitDTO request =
                new RequestDebitDTO(
                        userId,
                        amount,
                        WalletTransactionType.PAYMENT,
                        "Payment",
                        WalletReferenceType.WALLET_TRANSACTION,
                        referenceId
                );

        Wallet wallet =
                Wallet.builder()
                        .balance(BigDecimal.valueOf(50000))
                        .build();

        when(walletRepository.findByUserIdForUpdate(userId))
                .thenReturn(Optional.of(wallet));

        doThrow(new InsufficientBalanceException(wallet.getId(), wallet.getBalance(), amount))
                .when(walletHelper)
                .validateSufficientBalance(wallet, amount);

        // WHEN & THEN
        assertThatThrownBy(() -> useCase.execute(request))
                .isInstanceOf(InsufficientBalanceException.class);

        verify(walletTransactionRepository, never())
                .save(any());

        verify(walletMutationRepository, never())
                .save(any());
    }
}