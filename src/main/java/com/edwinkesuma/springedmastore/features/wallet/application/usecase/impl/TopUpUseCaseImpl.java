package com.edwinkesuma.springedmastore.features.wallet.application.usecase.impl;

import com.edwinkesuma.springedmastore.features.user.domain.service.WalletHelper;
import com.edwinkesuma.springedmastore.features.wallet.application.dto.RequestTopUpDTO;
import com.edwinkesuma.springedmastore.features.wallet.application.dto.ResponseWalletTransactionDTO;
import com.edwinkesuma.springedmastore.features.wallet.application.mapper.WalletTransactionMapper;
import com.edwinkesuma.springedmastore.features.wallet.application.usecase.TopUpUseCase;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TopUpUseCaseImpl implements TopUpUseCase {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final WalletMutationRepository walletMutationRepository;

    private final WalletHelper walletHelper;
    private final WalletTransactionMapper walletTransactionMapper;

    @Override
    @Transactional
    public ResponseWalletTransactionDTO execute(RequestTopUpDTO dto) {
        walletHelper.validateAmount(dto.amount());

        Wallet
                wallet =
                walletRepository.findByUserIdForUpdate(dto.userId())
                        .orElseThrow(() -> new EntityNotFoundException("Wallet not found"));

        BigDecimal balanceBefore = wallet.getBalance();

        // UPDATE BALANCE
        wallet.credit(dto.amount());

        BigDecimal balanceAfter = wallet.getBalance();

        // CREATE TRANSACTION
        WalletTransaction
                walletTransaction =
                WalletTransaction.builder()
                        .wallet(wallet)
                        .transactionCode(walletHelper.generateTransactionCode())
                        .type(WalletTransactionType.TOPUP)
                        .status(WalletTransactionStatus.SUCCESS)
                        .direction(TransactionDirection.CREDIT)
                        .amount(dto.amount())
                        .description(dto.description())
                        .build();

        walletTransactionRepository.save(walletTransaction);

        // CREATE MUTATION
        WalletMutation mutation = WalletMutation.builder()
                .wallet(wallet)
                .transaction(walletTransaction)
                .balanceBefore(balanceBefore)
                .amount(dto.amount())
                .balanceAfter(balanceAfter)
                .build();

        walletMutationRepository.save(mutation);

        return walletTransactionMapper.toDTO(walletTransaction);
    }
}
