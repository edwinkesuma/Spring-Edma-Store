package com.edwinkesuma.springedmastore.features.wallet.application.usecase.impl;

import com.edwinkesuma.springedmastore.common.exception.WalletNotFoundException;
import com.edwinkesuma.springedmastore.features.wallet.application.dto.RequestTopUpDTO;
import com.edwinkesuma.springedmastore.features.wallet.application.dto.ResponseWalletTransactionDTO;
import com.edwinkesuma.springedmastore.features.wallet.application.factory.WalletMutationFactory;
import com.edwinkesuma.springedmastore.features.wallet.application.factory.WalletTransactionFactory;
import com.edwinkesuma.springedmastore.features.wallet.application.mapper.WalletTransactionMapper;
import com.edwinkesuma.springedmastore.features.wallet.application.service.WalletHelper;
import com.edwinkesuma.springedmastore.features.wallet.application.usecase.TopUpUseCase;
import com.edwinkesuma.springedmastore.features.wallet.domain.entity.Wallet;
import com.edwinkesuma.springedmastore.features.wallet.domain.entity.WalletMutation;
import com.edwinkesuma.springedmastore.features.wallet.domain.entity.WalletTransaction;
import com.edwinkesuma.springedmastore.features.wallet.domain.repository.WalletMutationRepository;
import com.edwinkesuma.springedmastore.features.wallet.domain.repository.WalletRepository;
import com.edwinkesuma.springedmastore.features.wallet.domain.repository.WalletTransactionRepository;
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
    private final WalletTransactionFactory walletTransactionFactory;
    private final WalletMutationFactory walletMutationFactory;
    private final WalletTransactionMapper walletTransactionMapper;

    @Override
    @Transactional
    public ResponseWalletTransactionDTO execute(RequestTopUpDTO dto) {
        walletHelper.validateAmount(dto.amount());

        Wallet
                wallet =
                walletRepository.findByUserIdForUpdate(dto.userId())
                        .orElseThrow(() -> new WalletNotFoundException(dto.userId()));

        BigDecimal balanceBefore = wallet.getBalance();

        // UPDATE BALANCE
        wallet.credit(dto.amount());

        BigDecimal balanceAfter = wallet.getBalance();

        // CREATE TRANSACTION
        WalletTransaction
                walletTransaction = walletTransactionFactory.createCredit(wallet, dto);


        walletTransactionRepository.save(walletTransaction);

        // CREATE MUTATION
        WalletMutation
                mutation =
                walletMutationFactory.create(wallet, walletTransaction, balanceBefore, dto.amount(), balanceAfter);

        walletMutationRepository.save(mutation);

        ResponseWalletTransactionDTO transactionDTO = walletTransactionMapper.toDTO(walletTransaction);

        return transactionDTO.builder().balanceAfter(balanceAfter).balanceBefore(balanceBefore).currency("IDR").build();
    }
}
