package com.ephirious.entities;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeRate {
    Long id;
    Currency baseCurrency;
    Currency targetCurrency;
    BigDecimal rate;
}
