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
        List<CurrencyDTO> currencies = currencyService.getCurrenciesByCodes(List.of(base, target));

        Map<String, CurrencyDTO> currenciesMap = currencies.stream()
                .collect(Collectors.toMap(CurrencyDTO::getCode, currency -> currency));

        int expectedMapSize = 2;
        if (currenciesMap.size() != expectedMapSize) {
            throw new RuntimeException("Одной из валют не существует"); // ОБЯЗАТЕЛЬНО ИСПРАВИТЬ НА НОРМАЛЬНЫЕ ИСКЛЮЧЕНИЯ
        }

        return exchangeRateDao.findByCodeIDs(currenciesMap.get(base).getId(), currenciesMap.get(target).getId())
                .map(ExchangeRateDTO::fromExchangeRate)
                .map(rate -> new ExchangeRateDTO(
                        rate.getId(),
                        currenciesMap.get(base),
                        currenciesMap.get(target),
                        rate.getRate()
                ))
                .orElseThrow(() -> new RuntimeException("Обменного курса для данных валют не существует")); // Исправить исключения на кастомные
    }

    public List<ExchangeRateDTO> getExchangeRates() {
        List<ExchangeRate> rates = exchangeRateDao.findAll();

        List<Long> ids = rates.stream()
                .flatMap(rate -> Stream.of(rate.getBaseCurrency().getId(), rate.getTargetCurrency().getId()))
                .toList();

        List<CurrencyDTO> byIDs = currencyService.getCurrenciesByIDs(ids);
        Map<Long, CurrencyDTO> currencyMap = byIDs.stream()
                .collect(Collectors.toMap(CurrencyDTO::getId, currency -> currency));

        return rates.stream()
                .map(ExchangeRateDTO::fromExchangeRate)
                .map(rate -> new ExchangeRateDTO(
                        rate.getId(),
                        currencyMap.get(rate.getBase().getId()),
                        currencyMap.get(rate.getTarget().getId()),
                        rate.getRate()
                ))
                .toList();
    }

    public ExchangeRateDTO addExchangeRate(String base, String target, String rate) {
        List<CurrencyDTO> currencies = currencyService.getCurrenciesByCodes(List.of(base, target));

        Map<String, Long> currenciesMap = currencies.stream().collect(Collectors.toMap(
                CurrencyDTO::getCode, CurrencyDTO::getId
        ));

        ensureCurrenciesInMap(currenciesMap, base, target);

        boolean insertResult = exchangeRateDao.insert(currenciesMap.get(base), currenciesMap.get(target), new BigDecimal(rate));
        if (!insertResult) {
            throw new DaoException("Ошибка при вставке обменного курса в БД");
        }
        return getExchangeRate(base, target);
    }

    private void ensureCurrenciesInMap(Map<String, Long> map, String base, String target) {
        if (!map.containsKey(base)) {
            throw new NotFoundException("Валюты %s для обменного курса не была найдена".formatted(base));
        }
        if (!map.containsKey(target)) {
            throw new NotFoundException("Валюты %s для обменного курса не была найдена".formatted(target));
        }
    }
}
