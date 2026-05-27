-- V5__add_updated_at_to_categories.sql

ALTER TABLE categories
    ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;