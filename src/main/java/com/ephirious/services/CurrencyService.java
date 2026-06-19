package com.ephirious.services;

import com.ephirious.dao.CurrencyDao;
import com.ephirious.dto.CurrencyDTO;
import com.ephirious.entities.Currency;
import com.ephirious.exception.apiexception.service.currency.NotFoundException;

import java.util.List;
import java.util.Optional;
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
        return currencyDao.findByCode(code)
                .map(CurrencyDTO::fromCurrency)
                .orElseThrow(() -> new NotFoundException("Валюта с кодом %s не была найдена".formatted(code)));
    }

    public CurrencyDTO addCurrency(String code, String name, String sign) {
        Currency currency = new Currency(null, code, name, sign);
        return currencyDao.insert(currency)
                .map(CurrencyDTO::fromCurrency)
                .get();
    }
}
