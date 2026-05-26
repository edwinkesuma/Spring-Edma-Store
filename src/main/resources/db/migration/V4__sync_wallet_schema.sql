-- =========================================================
-- SYNC WALLET SCHEMA WITH JPA ENTITIES
-- =========================================================

-- =========================================================
-- wallet_mutations
-- =========================================================

-- add updated_at because WalletMutation extends BaseEntity
ALTER TABLE wallet_mutations
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- =========================================================
-- NOT NULL CONSTRAINTS
-- =========================================================

-- wallets
ALTER TABLE wallets
    ALTER COLUMN user_id SET NOT NULL;

ALTER TABLE wallets
    ALTER COLUMN currency SET NOT NULL;

ALTER TABLE wallets
    ALTER COLUMN balance SET NOT NULL;

ALTER TABLE wallets
    ALTER COLUMN is_frozen SET NOT NULL;

ALTER TABLE wallets
    ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE wallets
    ALTER COLUMN updated_at SET NOT NULL;

ALTER TABLE wallets
    ALTER COLUMN version SET NOT NULL;

-- wallet_transactions
ALTER TABLE wallet_transactions
    ALTER COLUMN wallet_id SET NOT NULL;

ALTER TABLE wallet_transactions
    ALTER COLUMN transaction_code SET NOT NULL;

ALTER TABLE wallet_transactions
    ALTER COLUMN type SET NOT NULL;

ALTER TABLE wallet_transactions
    ALTER COLUMN status SET NOT NULL;

ALTER TABLE wallet_transactions
    ALTER COLUMN direction SET NOT NULL;

ALTER TABLE wallet_transactions
    ALTER COLUMN amount SET NOT NULL;

ALTER TABLE wallet_transactions
    ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE wallet_transactions
    ALTER COLUMN updated_at SET NOT NULL;

-- wallet_mutations
ALTER TABLE wallet_mutations
    ALTER COLUMN wallet_id SET NOT NULL;

ALTER TABLE wallet_mutations
    ALTER COLUMN transaction_id SET NOT NULL;

ALTER TABLE wallet_mutations
    ALTER COLUMN balance_before SET NOT NULL;

ALTER TABLE wallet_mutations
    ALTER COLUMN amount SET NOT NULL;

ALTER TABLE wallet_mutations
    ALTER COLUMN balance_after SET NOT NULL;

ALTER TABLE wallet_mutations
    ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE wallet_mutations
    ALTER COLUMN updated_at SET NOT NULL;

-- =========================================================
-- FOREIGN KEYS
-- =========================================================

-- wallet_transactions -> wallets
ALTER TABLE wallet_transactions
    ADD CONSTRAINT fk_wallet_transactions_wallet
        FOREIGN KEY (wallet_id)
            REFERENCES wallets (id)
            ON DELETE RESTRICT;

-- wallet_mutations -> wallets
ALTER TABLE wallet_mutations
    ADD CONSTRAINT fk_wallet_mutations_wallet
        FOREIGN KEY (wallet_id)
            REFERENCES wallets (id)
            ON DELETE RESTRICT;

-- wallet_mutations -> wallet_transactions
ALTER TABLE wallet_mutations
    ADD CONSTRAINT fk_wallet_mutations_transaction
        FOREIGN KEY (transaction_id)
            REFERENCES wallet_transactions (id)
            ON DELETE RESTRICT;

-- =========================================================
-- UNIQUE CONSTRAINTS
-- =========================================================

-- one wallet per user
ALTER TABLE wallets
    ADD CONSTRAINT uk_wallet_user
        UNIQUE (user_id);

-- unique transaction code
ALTER TABLE wallet_transactions
    ADD CONSTRAINT uk_wallet_transaction_code
        UNIQUE (transaction_code);

-- because WalletMutation uses @OneToOne transaction
ALTER TABLE wallet_mutations
    ADD CONSTRAINT uk_wallet_mutation_transaction
        UNIQUE (transaction_id);