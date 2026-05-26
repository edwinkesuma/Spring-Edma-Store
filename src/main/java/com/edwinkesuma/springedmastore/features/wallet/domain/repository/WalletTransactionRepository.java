package com.edwinkesuma.springedmastore.features.wallet.domain.repository;

import com.edwinkesuma.springedmastore.features.wallet.domain.entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, UUID> {
}
