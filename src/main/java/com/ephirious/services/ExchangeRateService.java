package com.ephirious.services;

import com.ephirious.dao.ExchangeRateDao;
import com.ephirious.dto.ExchangeRateDTO;
import com.ephirious.exception.apiexception.service.exchangerate.NotExistRateException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ExchangeRateService {
    private final ExchangeRateDao exchangeRateDao;
    private final CurrencyService currencyService;

    public ExchangeRateService(ExchangeRateDao exchangeRateDao, CurrencyService currencyService) {
        this.exchangeRateDao = exchangeRateDao;
        this.currencyService = currencyService;
    }

    public ExchangeRateDTO getExchangeRate(String base, String target) {
        return exchangeRateDao.findByCodeID(base, target)
                .map(ExchangeRateDTO::fromExchangeRate)
                .orElseThrow(() -> new NotExistRateException("Обменного курса для данных валют не существует"));
    }

    public Optional<ExchangeRateDTO> findExchangeRate(String base, String target) {
        return exchangeRateDao.findByCodeID(base, target)
                .map(ExchangeRateDTO::fromExchangeRate);
    }

    public List<ExchangeRateDTO> getExchangeRates() {
        return exchangeRateDao.findAll().stream()
                .map(ExchangeRateDTO::fromExchangeRate)
                .toList();
    }

    public ExchangeRateDTO addExchangeRate(String base, String target, String rate) {
        if (currencyService.findCurrency(base).isEmpty()) {
            throw new NotExistRateException("Базовая валюта не найдена");
        }
        if (currencyService.findCurrency(target).isEmpty()) {
            throw new NotExistRateException("Целевая валюта не найдена");
        }

        return exchangeRateDao.insert(base, target, new BigDecimal(rate))
                .map(ExchangeRateDTO::fromExchangeRate)
                .orElseThrow();
    }

    public ExchangeRateDTO updateExchangeRate(String base, String target, String newRate) {
        return exchangeRateDao.update(base, target, new BigDecimal(newRate))
                .map(ExchangeRateDTO::fromExchangeRate)
                .orElseThrow(() -> new NotExistRateException("Обменного курса для данных валют не существует"));
    }
}
