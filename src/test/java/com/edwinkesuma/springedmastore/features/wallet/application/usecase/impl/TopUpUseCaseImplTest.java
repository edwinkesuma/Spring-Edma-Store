package com.edwinkesuma.springedmastore.features.wallet.application.usecase.impl;

import com.edwinkesuma.springedmastore.features.user.domain.service.WalletHelper;
import com.edwinkesuma.springedmastore.features.wallet.application.dto.RequestTopUpDTO;
import com.edwinkesuma.springedmastore.features.wallet.application.dto.ResponseWalletTransactionDTO;
import com.edwinkesuma.springedmastore.features.wallet.application.mapper.WalletTransactionMapper;
import com.edwinkesuma.springedmastore.features.wallet.domain.entity.Wallet;
import com.edwinkesuma.springedmastore.features.wallet.domain.entity.WalletMutation;
import com.edwinkesuma.springedmastore.features.wallet.domain.entity.WalletTransaction;
import com.edwinkesuma.springedmastore.features.wallet.domain.enums.TransactionDirection;
import com.edwinkesuma.springedmastore.features.wallet.domain.enums.WalletTransactionStatus;
import com.edwinkesuma.springedmastore.features.wallet.domain.enums.WalletTransactionType;
import com.edwinkesuma.springedmastore.features.wallet.domain.repository.WalletMutationRepository;
import com.edwinkesuma.springedmastore.features.wallet.domain.repository.WalletRepository;
import com.edwinkesuma.springedmastore.features.wallet.domain.repository.WalletTransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private WalletTransactionMapper walletTransactionMapper;

    @InjectMocks
    private TopUpUseCaseImpl topUpUseCase;

    @Test
    void execute_shouldTopUpSuccessfully() {

        // GIVEN
        UUID userId = UUID.randomUUID();

        RequestTopUpDTO request =
                new RequestTopUpDTO(
                        userId,
                        new BigDecimal("50000"),
                        "Top up saldo"
                );

        Wallet wallet = Wallet.builder()
                .balance(new BigDecimal("100000"))
                .build();

        ResponseWalletTransactionDTO responseDTO =
                ResponseWalletTransactionDTO.builder()
                        .transactionCode("TRX-001")
                        .build();

        when(walletRepository.findByUserIdForUpdate(userId))
                .thenReturn(Optional.of(wallet));

        when(walletHelper.generateTransactionCode())
                .thenReturn("TRX-001");

        when(walletTransactionMapper.toDTO(any(WalletTransaction.class)))
                .thenReturn(responseDTO);

        // WHEN
        ResponseWalletTransactionDTO result =
                topUpUseCase.execute(request);

        // THEN

        // VALIDATE BALANCE UPDATED
        assertEquals(
                new BigDecimal("150000"),
                wallet.getBalance()
        );

        // VALIDATE SAVE TRANSACTION
        ArgumentCaptor<WalletTransaction> transactionCaptor =
                ArgumentCaptor.forClass(WalletTransaction.class);

        verify(walletTransactionRepository)
                .save(transactionCaptor.capture());

        WalletTransaction savedTransaction =
                transactionCaptor.getValue();

        assertEquals(
                WalletTransactionType.TOPUP,
                savedTransaction.getType()
        );

        assertEquals(
                WalletTransactionStatus.SUCCESS,
                savedTransaction.getStatus()
        );

        assertEquals(
                TransactionDirection.CREDIT,
                savedTransaction.getDirection()
        );

        assertEquals(
                new BigDecimal("50000"),
                savedTransaction.getAmount()
        );

        // VALIDATE SAVE MUTATION
        ArgumentCaptor<WalletMutation> mutationCaptor =
                ArgumentCaptor.forClass(WalletMutation.class);

        verify(walletMutationRepository)
                .save(mutationCaptor.capture());

        WalletMutation savedMutation =
                mutationCaptor.getValue();

        assertEquals(
                new BigDecimal("100000"),
                savedMutation.getBalanceBefore()
        );

        assertEquals(
                new BigDecimal("150000"),
                savedMutation.getBalanceAfter()
        );

        assertEquals(
                new BigDecimal("50000"),
                savedMutation.getAmount()
        );

        // VALIDATE RESPONSE
        assertEquals(responseDTO, result);

        // VALIDATE HELPER CALLED
        verify(walletHelper)
                .validateAmount(new BigDecimal("50000"));

        verify(walletHelper)
                .generateTransactionCode();
    }

    @Test
    void execute_shouldThrowException_whenWalletNotFound() {

        // GIVEN
        UUID userId = UUID.randomUUID();

        RequestTopUpDTO request =
                new RequestTopUpDTO(
                        userId,
                        new BigDecimal("50000"),
                        "Top up saldo"
                );

        when(walletRepository.findByUserIdForUpdate(userId))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> topUpUseCase.execute(request)
                );

        assertEquals(
                "Wallet not found",
                exception.getMessage()
        );

        verify(walletTransactionRepository, never())
                .save(any());

        verify(walletMutationRepository, never())
                .save(any());
    }
}
