package com.edwinkesuma.springedmastore.features.wallet.application.usecase.impl;

import com.edwinkesuma.springedmastore.common.exception.WalletNotFoundException;
import com.edwinkesuma.springedmastore.features.wallet.application.usecase.GetBalanceUseCase;
import com.edwinkesuma.springedmastore.features.wallet.domain.entity.Wallet;
import com.edwinkesuma.springedmastore.features.wallet.domain.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetBalanceUseCaseImpl implements GetBalanceUseCase {
    private final WalletRepository walletRepository;

    @Override
    @Transactional
    public BigDecimal execute(UUID userId) {
        Wallet wallet = walletRepository.findByUserId(userId).orElseThrow(() -> new WalletNotFoundException(userId));

        return wallet.getBalance();
    }
}
