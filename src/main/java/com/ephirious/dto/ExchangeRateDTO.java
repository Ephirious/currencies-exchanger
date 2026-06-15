package com.ephirious.dto;

import com.ephirious.entities.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ExchangeRateDTO {
    private Long id;
    private Currency base;
    private Currency target;
    private BigDecimal rate;
}
