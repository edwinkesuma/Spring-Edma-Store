package com.edwinkesuma.springedmastore.features.wallet.application.factory;

import com.edwinkesuma.springedmastore.features.wallet.application.dto.RequestDebitDTO;
import com.edwinkesuma.springedmastore.features.wallet.application.dto.RequestTopUpDTO;
import com.edwinkesuma.springedmastore.features.wallet.application.service.WalletHelper;
import com.edwinkesuma.springedmastore.features.wallet.domain.entity.Wallet;
import com.edwinkesuma.springedmastore.features.wallet.domain.entity.WalletTransaction;
import com.edwinkesuma.springedmastore.features.wallet.domain.enums.TransactionDirection;
import com.edwinkesuma.springedmastore.features.wallet.domain.enums.WalletTransactionStatus;
import com.edwinkesuma.springedmastore.features.wallet.domain.enums.WalletTransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalletTransactionFactory {

    private final WalletHelper walletHelper;

    public WalletTransaction createDebit(
            Wallet wallet,
            RequestDebitDTO request
    ) {
        return WalletTransaction.builder()
                .wallet(wallet)
                .transactionCode(walletHelper.generateTransactionCode())
                .type(request.transactionType())
                .status(WalletTransactionStatus.SUCCESS)
                .direction(TransactionDirection.DEBIT)
                .amount(request.amount())
                .referenceType(request.referenceType())
                .referenceId(request.referenceId())
                .description(request.description())
                .build();
    }

    public WalletTransaction createCredit(
            Wallet wallet,
            RequestTopUpDTO request
    ) {
        return WalletTransaction.builder()
                .wallet(wallet)
                .transactionCode(walletHelper.generateTransactionCode())
                .type(WalletTransactionType.TOPUP)
                .status(WalletTransactionStatus.SUCCESS)
                .direction(TransactionDirection.CREDIT)
                .amount(request.amount())
                .referenceType(request.referenceType())
                .referenceId(request.referenceId())
                .description(request.description())
                .build();
    }
}