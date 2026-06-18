package com.ephirious.services;

import com.ephirious.dto.ExchangeDTO;
import com.ephirious.dto.ExchangeRateDTO;
import com.ephirious.exception.apiexception.service.exchange.ExchangeException;
import com.ephirious.util.ExchangeRateValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ExchangeService {
    private final ExchangeRateService rateService;

    public ExchangeService(ExchangeRateService rateService) {
        this.rateService = rateService;
    }

    public ExchangeDTO exchange(String codeFrom, String codeTo, String amount) {
        BigDecimal amountAsDecimal = new BigDecimal(amount);

        Optional<ExchangeRateDTO> forwardExchangeRate = rateService.findExchangeRate(codeFrom, codeTo);
        if (forwardExchangeRate.isPresent()) {
            return forwardExchange(forwardExchangeRate.get(), amountAsDecimal);
        }

        Optional<ExchangeRateDTO> reverseRate = rateService.findExchangeRate(codeTo, codeFrom);
        if (reverseRate.isPresent()) {
            return reverseExchange(reverseRate.get(), amountAsDecimal);
        }

        String crossTargetCode = "USD";
        Optional<ExchangeRateDTO> baseToUSD = rateService.findExchangeRate(codeFrom, crossTargetCode);
        Optional<ExchangeRateDTO> USDtoTarget = rateService.findExchangeRate(crossTargetCode, codeFrom);
        if (baseToUSD.isPresent() && USDtoTarget.isPresent()) {
            return crossExchange(baseToUSD.get(), USDtoTarget.get(), amountAsDecimal);
        }

        throw new ExchangeException("Для данной пары валют не был найден ни один курс обмена");
    }

    private ExchangeDTO forwardExchange(ExchangeRateDTO rate, BigDecimal amount) {
        return new ExchangeDTO(
                rate.getBase(),
                rate.getTarget(),
                rate.getRate(),
                amount,
                amount.multiply(rate.getRate())
        );
    }

    private ExchangeDTO reverseExchange(ExchangeRateDTO rate, BigDecimal amount) {
        BigDecimal reverseRate = BigDecimal.ONE.divide(rate.getRate(), ExchangeRateValidator.RATE_MAX_PRECISION, RoundingMode.HALF_EVEN);
        BigDecimal converted = amount.divide(rate.getRate(), ExchangeRateValidator.RATE_MAX_PRECISION, RoundingMode.HALF_EVEN);
        return new ExchangeDTO(
                rate.getTarget(),
                rate.getBase(),
                reverseRate,
                amount,
                converted
        );
    }

    private ExchangeDTO crossExchange(ExchangeRateDTO baseToHelpRate, ExchangeRateDTO helpRateToTarget, BigDecimal amount) {
        BigDecimal generalRate = baseToHelpRate.getRate().multiply(helpRateToTarget.getRate());
        BigDecimal converted = amount.multiply(generalRate);
        return new ExchangeDTO(
                baseToHelpRate.getBase(),
                helpRateToTarget.getTarget(),
                generalRate,
                amount,
                converted
        );
    }
}
