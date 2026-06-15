package com.ephirious.services;

import com.ephirious.dao.ExchangeRateDao;
import com.ephirious.dto.CurrencyDTO;
import com.ephirious.dto.ExchangeRateDTO;
import com.ephirious.entities.ExchangeRate;

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
}
