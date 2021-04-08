package com.chimyrys.currencyservice.service.api;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.ExchangeRate;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Interface for all banks services
 */
public interface CurrencyService {
    Optional<ExchangeRate> getCurrency(LocalDate date, Currency currencyFrom, Currency currencyTo);
    Optional<ExchangeRate> getBestBuyRateForMonth(Currency currencyFrom, Currency currencyTo);
    int getId();
    String getName();
}
