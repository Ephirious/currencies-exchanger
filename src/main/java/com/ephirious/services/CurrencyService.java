package com.ephirious.services;

import com.ephirious.dao.CurrencyDao;
import com.ephirious.dto.CurrencyDTO;
import com.ephirious.entities.Currency;
import com.ephirious.exception.apiexception.CurrencyNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class CurrencyService {
    private final CurrencyDao currencyDao;

    public CurrencyService(CurrencyDao currencyDao) {
        this.currencyDao = currencyDao;
    }

    public List<CurrencyDTO> getCurrencies() {
        List<Currency> currencies = currencyDao.findAll();
        return currencies.stream()
                .map(CurrencyDTO::fromCurrency)
                .collect(Collectors.toList());
    }

    public CurrencyDTO getCurrency(String code) {
        if (code == null) {
            throw new IllegalArgumentException("Параметр {code} не может быть нулевым");
        }

        return currencyDao.findByCode(code)
                .map(CurrencyDTO::fromCurrency)
                .orElseThrow(() -> new CurrencyNotFoundException(code));
    }

    public List<CurrencyDTO> getCurrenciesByIDs(List<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }

        return currencyDao.findByIDs(ids).stream()
                .map(CurrencyDTO::fromCurrency)
                .collect(Collectors.toList());
    }

    public CurrencyDTO addCurrency(String code, String name, String sign) {
        Currency currency = new Currency(null, code, name, sign);
        return currencyDao.insert(currency)
                .map(CurrencyDTO::fromCurrency)
                .get();
    }
}
