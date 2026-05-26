package com.edwinkesuma.springedmastore.common.exception;

import java.math.BigDecimal;
import java.util.UUID;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(UUID walletId, BigDecimal balance, BigDecimal requestedAmount) {
        super("Insufficient balance for wallet " + walletId);
    }
}
