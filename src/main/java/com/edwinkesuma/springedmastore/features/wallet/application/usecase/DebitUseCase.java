package com.edwinkesuma.springedmastore.features.wallet.application.usecase;

import com.edwinkesuma.springedmastore.features.wallet.application.dto.RequestDebitDTO;
import com.edwinkesuma.springedmastore.features.wallet.application.dto.ResponseWalletTransactionDTO;

public interface DebitUseCase {
    ResponseWalletTransactionDTO execute(RequestDebitDTO request);
}
