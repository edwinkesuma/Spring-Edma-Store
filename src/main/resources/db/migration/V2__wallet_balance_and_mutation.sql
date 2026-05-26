-- =========================================
-- ADD BALANCE & IS_FROZEN TO WALLETS
-- =========================================
ALTER TABLE wallets
    ADD COLUMN balance NUMERIC(19, 2) NOT NULL DEFAULT 0,
    ADD COLUMN is_frozen BOOLEAN NOT NULL DEFAULT FALSE;

-- =========================================
-- ADD NEW COLUMNS TO WALLET_TRANSACTIONS
-- =========================================
ALTER TABLE wallet_transactions
    ADD COLUMN transaction_code VARCHAR(100),
    ADD COLUMN status VARCHAR(20),
    ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN external_reference VARCHAR(100);

-- =========================================
-- BACKFILL EXISTING DATA
-- =========================================
UPDATE wallet_transactions
SET transaction_code = gen_random_uuid()::text,
    status = 'SUCCESS'
WHERE transaction_code IS NULL;

-- =========================================
-- ADD CONSTRAINTS AFTER BACKFILL
-- =========================================
ALTER TABLE wallet_transactions
    ALTER COLUMN transaction_code SET NOT NULL,
ALTER
COLUMN status SET NOT NULL;

ALTER TABLE wallet_transactions
    ADD CONSTRAINT uq_wallet_transaction_code
        UNIQUE (transaction_code);

-- =========================================
-- CREATE WALLET MUTATIONS
-- =========================================
CREATE TABLE wallet_mutations
(
    id             UUID PRIMARY KEY        DEFAULT gen_random_uuid(),

    wallet_id      UUID           NOT NULL,

    transaction_id UUID           NOT NULL,

    balance_before NUMERIC(19, 2) NOT NULL,

    amount         NUMERIC(19, 2) NOT NULL,

    balance_after  NUMERIC(19, 2) NOT NULL,

    created_at     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_wallet_mutation_wallet
        FOREIGN KEY (wallet_id)
            REFERENCES wallets (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_wallet_mutation_transaction
        FOREIGN KEY (transaction_id)
            REFERENCES wallet_transactions (id)
            ON DELETE CASCADE
);

-- =========================================
-- INDEXES
-- =========================================
CREATE INDEX idx_wallet_mutations_wallet_id
    ON wallet_mutations (wallet_id);

CREATE INDEX idx_wallet_mutations_transaction_id
    ON wallet_mutations (transaction_id);