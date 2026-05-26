package com.edwinkesuma.springedmastore.features.wallet.application.factory;

import com.edwinkesuma.springedmastore.features.wallet.domain.entity.Wallet;
import com.edwinkesuma.springedmastore.features.wallet.domain.entity.WalletMutation;
import com.edwinkesuma.springedmastore.features.wallet.domain.entity.WalletTransaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WalletMutationFactory {

    public WalletMutation create(
            Wallet wallet,
            WalletTransaction transaction,
            BigDecimal balanceBefore,
            BigDecimal amount,
            BigDecimal balanceAfter
    ) {
        return WalletMutation.builder()
                .wallet(wallet)
                .transaction(transaction)
                .balanceBefore(balanceBefore)
                .amount(amount)
                .balanceAfter(balanceAfter)
                .build();
    }
}
