package com.ephirious.services;

import com.ephirious.dto.ExchangeDTO;
import com.ephirious.dto.ExchangeRateDTO;
import com.ephirious.exception.apiexception.service.exchange.ExchangeException;
import com.ephirious.util.ExchangeRateValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ExchangeService {
    private static final int AMOUNT_PRECISION = 2;

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
        Optional<ExchangeRateDTO> USDToBase = rateService.findExchangeRate(crossTargetCode, codeTo);
        Optional<ExchangeRateDTO> USDToTarget = rateService.findExchangeRate(crossTargetCode, codeFrom);
        if (USDToBase.isPresent() && USDToTarget.isPresent()) {
            return crossExchange(USDToBase.get(), USDToTarget.get(), amountAsDecimal);
        }

        throw new ExchangeException("Для данной пары валют не был найден ни один курс обмена");
    }

    private ExchangeDTO forwardExchange(ExchangeRateDTO rate, BigDecimal amount) {
        return new ExchangeDTO(
                rate.getBaseCurrency(),
                rate.getTargetCurrency(),
                rate.getRate(),
                amount,
                amount.multiply(rate.getRate())
                        .setScale(AMOUNT_PRECISION, RoundingMode.HALF_EVEN)
        );
    }

    private ExchangeDTO reverseExchange(ExchangeRateDTO rate, BigDecimal amount) {
        BigDecimal reverseRate = BigDecimal.ONE.divide(rate.getRate(), ExchangeRateValidator.RATE_MAX_PRECISION, RoundingMode.HALF_EVEN);
        BigDecimal converted = amount.divide(rate.getRate(), AMOUNT_PRECISION, RoundingMode.HALF_EVEN);
        return new ExchangeDTO(
                rate.getTargetCurrency(),
                rate.getBaseCurrency(),
                reverseRate,
                amount,
                converted
        );
    }

    private ExchangeDTO crossExchange(ExchangeRateDTO helpToBase, ExchangeRateDTO helpToTarget, BigDecimal amount) {
        BigDecimal generalRate = helpToBase.getRate().divide(helpToTarget.getRate(),
                ExchangeRateValidator.RATE_MAX_PRECISION,
                RoundingMode.HALF_EVEN
        );
        BigDecimal converted = amount.multiply(helpToBase.getRate())
                .divide(helpToTarget.getRate(), AMOUNT_PRECISION, RoundingMode.HALF_EVEN);
        return new ExchangeDTO(
                helpToBase.getTargetCurrency(),
                helpToTarget.getTargetCurrency(),
                generalRate,
                amount,
                converted
        );
    }
}
