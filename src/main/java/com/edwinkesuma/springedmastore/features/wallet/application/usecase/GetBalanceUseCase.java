package com.edwinkesuma.springedmastore.features.wallet.application.usecase;

import java.math.BigDecimal;
import java.util.UUID;

public interface GetBalanceUseCase {
    BigDecimal execute(UUID userId);
}
