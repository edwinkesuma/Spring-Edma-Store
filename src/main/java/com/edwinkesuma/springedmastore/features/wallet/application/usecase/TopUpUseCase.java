package com.edwinkesuma.springedmastore.features.wallet.application.usecase;

import com.edwinkesuma.springedmastore.features.wallet.application.dto.RequestTopUpDTO;
import com.edwinkesuma.springedmastore.features.wallet.application.dto.ResponseWalletTransactionDTO;

public interface TopUpUseCase {
    ResponseWalletTransactionDTO execute(RequestTopUpDTO dto);
}
