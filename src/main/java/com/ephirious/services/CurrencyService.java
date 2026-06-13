package com.ephirious.services;

import com.ephirious.dao.CurrencyDao;
import com.ephirious.dto.CurrencyDTO;
import com.ephirious.entities.Currency;
import com.ephirious.exception.CurrencyNotFoundException;

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

        String upperCaseCode = code.toUpperCase();

        return currencyDao.findByCode(upperCaseCode)
                .map(CurrencyDTO::fromCurrency)
                .orElseThrow(() -> new CurrencyNotFoundException(upperCaseCode));
    }
}
