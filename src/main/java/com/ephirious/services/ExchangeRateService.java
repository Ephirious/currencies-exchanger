package com.ephirious.services;

import com.ephirious.dao.ExchangeRateDao;
import com.ephirious.dto.CurrencyDTO;
import com.ephirious.dto.ExchangeRateDTO;
import com.ephirious.entities.ExchangeRate;
import com.ephirious.exception.apiexception.dao.DaoException;
import com.ephirious.exception.apiexception.service.currency.NotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExchangeRateService {
    private final CurrencyService currencyService;
    private final ExchangeRateDao exchangeRateDao;

    public ExchangeRateService(CurrencyService currencyService, ExchangeRateDao exchangeRateDao) {
        this.currencyService = currencyService;
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
