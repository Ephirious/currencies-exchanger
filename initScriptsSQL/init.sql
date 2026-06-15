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
);

INSERT INTO currencies (code, fullname, sign)
VALUES
    ('USD', 'US Dollar', '$'),
    ('EUR', 'Euro', '€'),
    ('RUB', 'Russian Ruble', '₽'),
    ('GBP', 'British Pound', '£'),
    ('JPY', 'Japanese Yen', '¥'),
    ('CNY', 'Chinese Yuan', '¥'),
    ('KZT', 'Kazakhstani Tenge', '₸'),
    ('BYN', 'Belarusian Ruble', 'Br'),
    ('TRY', 'Turkish Lira', '₺'),
    ('AED', 'UAE Dirham', 'د.إ');

INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate)
VALUES
    (1, 2, 0.861580),
    (1, 3, 72.524040),
    (1, 4, 0.784500),
    (1, 5, 156.420000),
    (1, 6, 7.243500),
    (1, 7, 489.555800),
    (1, 8, 3.265000),
    (1, 9, 32.450000),
    (1, 10, 3.672500),

    (2, 1, 1.160658),
    (2, 3, 84.175404),

    (3, 1, 0.013789),
    (3, 2, 0.011880),
    (3, 7, 6.750201);