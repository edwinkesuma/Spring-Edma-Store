package com.edwinkesuma.springedmastore.features.wallet.domain.repository;

import com.edwinkesuma.springedmastore.features.wallet.domain.entity.WalletMutation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WalletMutationRepository extends JpaRepository<WalletMutation, UUID> {
}
