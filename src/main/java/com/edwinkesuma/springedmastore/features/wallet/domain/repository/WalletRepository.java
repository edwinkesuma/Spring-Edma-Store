package com.edwinkesuma.springedmastore.features.wallet.domain.repository;

import com.edwinkesuma.springedmastore.features.wallet.domain.entity.Wallet;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    Optional<Wallet> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);

    boolean existsByUserIdAndIsFrozenFalse(UUID userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({
            @QueryHint(
                    name = "jakarta.persistence.lock.timeout",
                    value = "5000"
            )
    })
    @Query("""
                SELECT w
                FROM Wallet w
                WHERE w.id = :walletId
            """)
    Optional<Wallet> findByIdForUpdate(
            @Param("walletId") UUID walletId
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({
            @QueryHint(
                    name = "jakarta.persistence.lock.timeout",
                    value = "5000"
            )
    })
    @Query("""
                SELECT w
                FROM Wallet w
                WHERE w.user.id = :userId
            """)
    Optional<Wallet> findByUserIdForUpdate(
            @Param("userId") UUID userId
    );
}