package com.ephirious.entities;


import lombok.Value;

import java.math.BigDecimal;

@Value
public class ExchangeRate {
    Long id;
    Currency baseCurrency;
    Currency targetCurrency;
    BigDecimal rate;
}
