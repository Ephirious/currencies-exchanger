package com.ephirious.dto;

import com.ephirious.entities.Currency;
import com.ephirious.entities.ExchangeRate;
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

    public static ExchangeRateDTO fromExchangeRate(ExchangeRate exchangeRate) {
        return new ExchangeRateDTO(
                exchangeRate.getId(),
                CurrencyDTO.fromCurrency(exchangeRate.getBaseCurrency()),
                CurrencyDTO.fromCurrency(exchangeRate.getTargetCurrency()),
                exchangeRate.getRate()
        );
    }
}
