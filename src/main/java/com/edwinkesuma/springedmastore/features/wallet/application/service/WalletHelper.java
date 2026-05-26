package com.edwinkesuma.springedmastore.features.wallet.application.service;

import com.edwinkesuma.springedmastore.common.exception.InsufficientBalanceException;
import com.edwinkesuma.springedmastore.features.wallet.domain.entity.Wallet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class WalletHelper {
    public void validateAmount(BigDecimal amount) {

        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be greater than zero"
            );
        }

        if (amount.scale() > 2) {
            throw new IllegalArgumentException(
                    "Amount cannot have more than 2 decimal places"
            );
        }
    }

    public String generateTransactionCode() {
        return "TXN-" + UUID.randomUUID();
    }

    public void validateSufficientBalance(
            Wallet wallet,
            BigDecimal amount
    ) {
        if (!wallet.hasSufficientBalance(amount)) {
            throw new InsufficientBalanceException(
                    wallet.getId(),
                    wallet.getBalance(),
                    amount
            );
        }
    }
}
