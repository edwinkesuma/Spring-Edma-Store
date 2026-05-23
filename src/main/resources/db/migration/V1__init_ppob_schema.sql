-- =========================================
-- V1__init_ppob_schema.sql
-- =========================================

-- =========================================
-- EXTENSIONS
-- =========================================
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- =========================================
-- USERS
-- =========================================
CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                       username VARCHAR(100) UNIQUE,
                       email VARCHAR(150) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,

                       role VARCHAR(20) NOT NULL,

                       phone VARCHAR(30),

                       is_active BOOLEAN NOT NULL DEFAULT TRUE,

                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email
    ON users(email);

CREATE INDEX idx_users_role
    ON users(role);

-- =========================================
-- RESELLER PROFILES
-- =========================================
CREATE TABLE reseller_profiles (
                                   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                                   user_id UUID NOT NULL UNIQUE,

                                   store_name VARCHAR(150),

                                   level INT NOT NULL DEFAULT 1,

                                   total_topup NUMERIC(19,2) NOT NULL DEFAULT 0,

                                   approved_at TIMESTAMP,

                                   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                   CONSTRAINT fk_reseller_profile_user
                                       FOREIGN KEY (user_id)
                                           REFERENCES users(id)
                                           ON DELETE CASCADE
);

-- =========================================
-- WALLETS
-- =========================================
CREATE TABLE wallets (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                         user_id UUID NOT NULL UNIQUE,

                         currency VARCHAR(10) NOT NULL DEFAULT 'IDR',

                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                         CONSTRAINT fk_wallet_user
                             FOREIGN KEY (user_id)
                                 REFERENCES users(id)
                                 ON DELETE CASCADE
);

-- =========================================
-- WALLET TRANSACTIONS
-- =========================================
CREATE TABLE wallet_transactions (
                                     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                                     wallet_id UUID NOT NULL,

                                     type VARCHAR(30) NOT NULL,

                                     direction VARCHAR(10) NOT NULL,

                                     amount NUMERIC(19,2) NOT NULL,

                                     reference_type VARCHAR(30),
                                     reference_id UUID,

                                     description TEXT,

                                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                     CONSTRAINT fk_wallet_transaction_wallet
                                         FOREIGN KEY (wallet_id)
                                             REFERENCES wallets(id)
                                             ON DELETE CASCADE
);

CREATE INDEX idx_wallet_transactions_wallet_id
    ON wallet_transactions(wallet_id);

CREATE INDEX idx_wallet_transactions_reference
    ON wallet_transactions(reference_type, reference_id);

-- =========================================
-- CATEGORIES
-- =========================================
CREATE TABLE categories (
                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                            name VARCHAR(100) NOT NULL,

                            slug VARCHAR(100) NOT NULL UNIQUE,

                            icon_url TEXT,

                            type VARCHAR(30) NOT NULL,

                            sort_order INT NOT NULL DEFAULT 0,

                            is_active BOOLEAN NOT NULL DEFAULT TRUE,

                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_categories_slug
    ON categories(slug);

-- =========================================
-- PROVIDERS
-- =========================================
CREATE TABLE providers (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                           name VARCHAR(150) NOT NULL,

                           code VARCHAR(50) NOT NULL UNIQUE,

                           is_active BOOLEAN NOT NULL DEFAULT TRUE,

                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =========================================
-- PRODUCTS
-- =========================================
CREATE TABLE products (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                          category_id UUID NOT NULL,
                          provider_id UUID NOT NULL,

                          name VARCHAR(150) NOT NULL,

                          slug VARCHAR(150) NOT NULL UNIQUE,

                          logo_url TEXT,

                          input_label VARCHAR(100),

                          input_type VARCHAR(30) NOT NULL,

                          is_active BOOLEAN NOT NULL DEFAULT TRUE,

                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT fk_product_category
                              FOREIGN KEY (category_id)
                                  REFERENCES categories(id),

                          CONSTRAINT fk_product_provider
                              FOREIGN KEY (provider_id)
                                  REFERENCES providers(id)
);

CREATE INDEX idx_products_slug
    ON products(slug);

CREATE INDEX idx_products_category_id
    ON products(category_id);

-- =========================================
-- PRODUCT ITEMS
-- =========================================
CREATE TABLE product_items (
                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                               product_id UUID NOT NULL,

                               name VARCHAR(150) NOT NULL,

                               digiflazz_sku VARCHAR(100) UNIQUE NOT NULL,

                               base_price NUMERIC(19,2) NOT NULL,

                               sell_price NUMERIC(19,2) NOT NULL,

                               stock INT,

                               is_unlimited_stock BOOLEAN NOT NULL DEFAULT TRUE,

                               is_active BOOLEAN NOT NULL DEFAULT TRUE,

                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT fk_product_item_product
                                   FOREIGN KEY (product_id)
                                       REFERENCES products(id)
                                       ON DELETE CASCADE
);

CREATE INDEX idx_product_items_product_id
    ON product_items(product_id);

CREATE INDEX idx_product_items_digiflazz_sku
    ON product_items(digiflazz_sku);

-- =========================================
-- RESELLER PRICES
-- =========================================
CREATE TABLE reseller_prices (
                                 id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                                 product_item_id UUID NOT NULL,

                                 reseller_level INT NOT NULL,

                                 sell_price NUMERIC(19,2) NOT NULL,

                                 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                 CONSTRAINT fk_reseller_price_product_item
                                     FOREIGN KEY (product_item_id)
                                         REFERENCES product_items(id)
                                         ON DELETE CASCADE,

                                 CONSTRAINT uq_reseller_price
                                     UNIQUE(product_item_id, reseller_level)
);

-- =========================================
-- ORDERS
-- =========================================
CREATE TABLE orders (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                        user_id UUID,

                        guest_email VARCHAR(150),

                        order_code VARCHAR(100) NOT NULL UNIQUE,

                        target_id VARCHAR(100) NOT NULL,

                        status VARCHAR(30) NOT NULL,

                        amount NUMERIC(19,2) NOT NULL,

                        paid_at TIMESTAMP,

                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                        CONSTRAINT fk_order_user
                            FOREIGN KEY (user_id)
                                REFERENCES users(id)
);

CREATE INDEX idx_orders_order_code
    ON orders(order_code);

CREATE INDEX idx_orders_status
    ON orders(status);

-- =========================================
-- ORDER ITEMS
-- =========================================
CREATE TABLE order_items (
                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                             order_id UUID NOT NULL,

                             product_item_id UUID NOT NULL,

                             item_name VARCHAR(150) NOT NULL,

                             buy_price NUMERIC(19,2) NOT NULL,

                             sell_price NUMERIC(19,2) NOT NULL,

                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                             CONSTRAINT fk_order_item_order
                                 FOREIGN KEY (order_id)
                                     REFERENCES orders(id)
                                     ON DELETE CASCADE,

                             CONSTRAINT fk_order_item_product_item
                                 FOREIGN KEY (product_item_id)
                                     REFERENCES product_items(id)
);

CREATE INDEX idx_order_items_order_id
    ON order_items(order_id);

-- =========================================
-- PAYMENTS
-- =========================================
CREATE TABLE payments (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                          order_id UUID NOT NULL UNIQUE,

                          gateway VARCHAR(30) NOT NULL,

                          channel VARCHAR(50),

                          gateway_reference VARCHAR(100),

                          gross_amount NUMERIC(19,2) NOT NULL,

                          status VARCHAR(20) NOT NULL,

                          raw_response JSONB,

                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT fk_payment_order
                              FOREIGN KEY (order_id)
                                  REFERENCES orders(id)
                                  ON DELETE CASCADE
);

CREATE INDEX idx_payments_gateway_reference
    ON payments(gateway_reference);

CREATE INDEX idx_payments_status
    ON payments(status);

-- =========================================
-- RESELLER TOPUPS
-- =========================================
CREATE TABLE reseller_topups (
                                 id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                                 reseller_id UUID NOT NULL,

                                 amount NUMERIC(19,2) NOT NULL,

                                 status VARCHAR(20) NOT NULL,

                                 proof_url TEXT,

                                 confirmed_at TIMESTAMP,

                                 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                 CONSTRAINT fk_reseller_topup_reseller
                                     FOREIGN KEY (reseller_id)
                                         REFERENCES reseller_profiles(id)
);

-- =========================================
-- DIGIFLAZZ TRANSACTIONS
-- =========================================
CREATE TABLE digiflazz_transactions (
                                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                                        order_item_id UUID NOT NULL UNIQUE,

                                        ref_id VARCHAR(100),

                                        buyer_sku_code VARCHAR(100) NOT NULL,

                                        customer_no VARCHAR(100) NOT NULL,

                                        status VARCHAR(20) NOT NULL,

                                        sn VARCHAR(255),

                                        message TEXT,

                                        raw_response JSONB,

                                        processed_at TIMESTAMP,

                                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                        CONSTRAINT fk_digiflazz_transaction_order_item
                                            FOREIGN KEY (order_item_id)
                                                REFERENCES order_items(id)
);

CREATE INDEX idx_digiflazz_ref_id
    ON digiflazz_transactions(ref_id);

CREATE INDEX idx_digiflazz_status
    ON digiflazz_transactions(status);

-- =========================================
-- BANNERS
-- =========================================
CREATE TABLE banners (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                         title VARCHAR(150),

                         description TEXT,

                         image_url TEXT NOT NULL,

                         redirect_type VARCHAR(30) NOT NULL,

                         redirect_value VARCHAR(255),

                         start_at TIMESTAMP,
                         end_at TIMESTAMP,

                         sort_order INT NOT NULL DEFAULT 0,

                         is_active BOOLEAN NOT NULL DEFAULT TRUE,

                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =========================================
-- AUDIT LOGS
-- =========================================
CREATE TABLE audit_logs (
                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                            user_id UUID,

                            action VARCHAR(100) NOT NULL,

                            entity VARCHAR(100),

                            entity_id VARCHAR(100),

                            old_value JSONB,
                            new_value JSONB,

                            ip_address VARCHAR(50),

                            user_agent TEXT,

                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                            CONSTRAINT fk_audit_log_user
                                FOREIGN KEY (user_id)
                                    REFERENCES users(id)
);