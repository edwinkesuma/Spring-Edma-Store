-- remove old foreign keys from V2
ALTER TABLE wallet_mutations
DROP
CONSTRAINT IF EXISTS fk_wallet_mutation_wallet,
    DROP
CONSTRAINT IF EXISTS fk_wallet_mutation_transaction;

-- ensure canonical foreign keys exist
ALTER TABLE wallet_mutations
DROP
CONSTRAINT IF EXISTS fk_wallet_mutations_wallet,
    DROP
CONSTRAINT IF EXISTS fk_wallet_mutations_transaction;

ALTER TABLE wallet_mutations
    ADD CONSTRAINT fk_wallet_mutations_wallet
        FOREIGN KEY (wallet_id)
            REFERENCES wallets (id)
            ON DELETE RESTRICT;

ALTER TABLE wallet_mutations
    ADD CONSTRAINT fk_wallet_mutations_transaction
        FOREIGN KEY (transaction_id)
            REFERENCES wallet_transactions (id)
            ON DELETE RESTRICT;