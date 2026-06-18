package com.ephirious.dto;

import com.ephirious.entities.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyDTO {
    Long id;
    String code;
    String name;
    String sign;

    public static CurrencyDTO fromCurrency(Currency currency) {
        return new CurrencyDTO(
          currency.getId(),
          currency.getCode(),
          currency.getName(),
          currency.getSign()
        );
    }
}
