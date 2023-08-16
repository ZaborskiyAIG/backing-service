CREATE SEQUENCE IF NOT EXISTS bank_accounts_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS beneficiaries_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS transaction_history_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE beneficiaries
(
    id          BIGINT NOT NULL,
    username    VARCHAR(255) UNIQUE,
    CONSTRAINT pk_beneficiaries PRIMARY KEY (id)
);

CREATE TABLE bank_accounts
(
    id              BIGINT NOT NULL,
    balance         NUMERIC(38,2),
    pin_code        VARCHAR(4) NOT NULL,
    beneficiary_id  BIGINT NOT NULL,
    account_number  VARCHAR(16) NOT NULL UNIQUE,
    CONSTRAINT pk_bank_accounts PRIMARY KEY (id)
);

CREATE TABLE transaction_history
(
    id                          BIGINT NOT NULL,
    price                       NUMERIC(38, 2),
    date                        TIMESTAMP,
    bank_account_id             BIGINT,
    transfer_to_bank_account_id BIGINT,
    transaction_type            VARCHAR(255) CHECK (transaction_type IN ('DEPOSIT', 'WITHDRAW', 'TRANSFER')),
    CONSTRAINT pk_transaction_history PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS bank_accounts
    ADD CONSTRAINT fk_bank_accounts_on_beneficiary FOREIGN KEY (beneficiary_id) REFERENCES beneficiaries;

ALTER TABLE IF EXISTS transaction_history
    ADD CONSTRAINT fk_transaction_history_on_bank_account FOREIGN KEY (bank_account_id) REFERENCES bank_accounts;

ALTER TABLE IF EXISTS transaction_history
    ADD CONSTRAINT fk_transaction_history_on_transfer_bank_account FOREIGN KEY (transfer_to_bank_account_id) REFERENCES bank_accounts;

CREATE UNIQUE INDEX username_index ON beneficiaries (username);
CREATE UNIQUE INDEX account_number_index ON bank_accounts (account_number);