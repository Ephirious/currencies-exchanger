CREATE TABLE currencies(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(3) NOT NULL UNIQUE CHECK (code = UPPER(code)),
    fullname VARCHAR(64) NOT NULL,
    sign VARCHAR(4) NOT NULL
);

CREATE TABLE exchange_rates (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    base_currency_id BIGINT NOT NULL REFERENCES currencies(id),
    target_currency_id BIGINT NOT NULL REFERENCES currencies(id),
    rate DECIMAL NOT NULL CHECK(rate = round(rate, 6)),
    CONSTRAINT unique_currency_pair UNIQUE (base_currency_id, target_currency_id),
    CONSTRAINT check_different_currencies CHECK (base_currency_id != target_currency_id)
)

