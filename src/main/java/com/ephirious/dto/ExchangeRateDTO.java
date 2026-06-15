package com.ephirious.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ExchangeRateDTO {
    private Long id;
    private CurrencyDTO base;
    private CurrencyDTO target;
    private BigDecimal rate;
}
