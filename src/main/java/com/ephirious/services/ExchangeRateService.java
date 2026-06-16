package com.ephirious.services;

import com.ephirious.dao.ExchangeRateDao;
import com.ephirious.dto.ExchangeRateDTO;

import java.math.BigDecimal;
import java.util.List;

public class ExchangeRateService {
    private final ExchangeRateDao exchangeRateDao;

    public ExchangeRateService(ExchangeRateDao exchangeRateDao) {
        this.exchangeRateDao = exchangeRateDao;
    }

    public ExchangeRateDTO getExchangeRate(String base, String target) {
        return exchangeRateDao.findByCodeID(base, target)
                .map(ExchangeRateDTO::fromExchangeRate)
                .orElseThrow(() -> new RuntimeException("Обменного курса для данных валют не существует")); // Исправить исключения на кастомные
    }

    public List<ExchangeRateDTO> getExchangeRates() {
        return exchangeRateDao.findAll().stream()
                .map(ExchangeRateDTO::fromExchangeRate)
                .toList();
    }

    public ExchangeRateDTO addExchangeRate(String base, String target, String rate) {
        return exchangeRateDao.insert(base, target, new BigDecimal(rate))
                .map(ExchangeRateDTO::fromExchangeRate)
                .get();
    }
}
