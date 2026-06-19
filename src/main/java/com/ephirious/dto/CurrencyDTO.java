package com.ephirious.dto;

import com.ephirious.entities.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyDTO {
    private Long id;
    private String code;
    private String name;
    private String sign;

    public static CurrencyDTO fromCurrency(Currency currency) {
        return new CurrencyDTO(
          currency.getId(),
          currency.getCode(),
          currency.getName(),
          currency.getSign()
        );
    }
}
