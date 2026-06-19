CREATE TABLE currencies(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    code VARCHAR(3) NOT NULL,
    fullname VARCHAR(64) NOT NULL,
    sign VARCHAR(4) NOT NULL,

    CONSTRAINT pk_currencies PRIMARY KEY (id),
    CONSTRAINT unique_code_check UNIQUE (code),
    CONSTRAINT upper_code_check CHECK (code = UPPER(code))
);

CREATE TABLE exchange_rates (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    base_currency_id BIGINT NOT NULL,
    target_currency_id BIGINT NOT NULL,
    rate DECIMAL NOT NULL,

    CONSTRAINT pk_exchange_rates PRIMARY KEY (id),
    CONSTRAINT fk_base_currency_id FOREIGN KEY (base_currency_id) REFERENCES currencies(id),
    CONSTRAINT fk_target_currency_id FOREIGN KEY (target_currency_id) REFERENCES currencies(id),
    CONSTRAINT check_rate_scale CHECK (rate = round(rate, 6)),
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