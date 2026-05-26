package com.edwinkesuma.springedmastore.features.wallet.application.usecase.impl;

import com.edwinkesuma.springedmastore.common.exception.WalletNotFoundException;
import com.edwinkesuma.springedmastore.features.wallet.application.dto.RequestDebitDTO;
import com.edwinkesuma.springedmastore.features.wallet.application.dto.ResponseWalletTransactionDTO;
import com.edwinkesuma.springedmastore.features.wallet.application.factory.WalletMutationFactory;
import com.edwinkesuma.springedmastore.features.wallet.application.factory.WalletTransactionFactory;
import com.edwinkesuma.springedmastore.features.wallet.application.mapper.WalletTransactionMapper;
import com.edwinkesuma.springedmastore.features.wallet.application.service.WalletHelper;
import com.edwinkesuma.springedmastore.features.wallet.application.usecase.DebitUseCase;
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
public class DebitUseCaseImpl implements DebitUseCase {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final WalletMutationRepository walletMutationRepository;

    private final WalletHelper walletHelper;
    private final WalletTransactionFactory walletTransactionFactory;
    private final WalletMutationFactory walletMutationFactory;
    private final WalletTransactionMapper walletTransactionMapper;

    @Override
    @Transactional
    public ResponseWalletTransactionDTO execute(RequestDebitDTO request) {
        walletHelper.validateAmount(request.amount());

        Wallet
                wallet =
                walletRepository.findByUserIdForUpdate(request.userId())
                        .orElseThrow(() -> new WalletNotFoundException(request.userId()));


        // VALIDATE BALANCE
        walletHelper.validateSufficientBalance(wallet, request.amount());


        // UPDATE BALANCE
        BigDecimal balanceBefore = wallet.getBalance();
        wallet.debit(request.amount());
        BigDecimal balanceAfter = wallet.getBalance();

        // CREATE TRANSACTION
        WalletTransaction transaction = walletTransactionFactory.createDebit(wallet, request);
        walletTransactionRepository.save(transaction);

        // CREATE MUTATION
        WalletMutation
                mutation =
                walletMutationFactory.create(wallet, transaction, balanceBefore, request.amount(), balanceAfter);
        walletMutationRepository.save(mutation);

        return walletTransactionMapper.toDTO(transaction);
    }
}
