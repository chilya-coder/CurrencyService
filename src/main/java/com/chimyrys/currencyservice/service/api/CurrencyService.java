package com.chimyrys.currencyservice.service.api;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.ExchangeRate;

import java.time.LocalDate;

/**
 * Interface for all banks services
 */
public interface CurrencyService {
    ExchangeRate getCurrency(LocalDate date, Currency currencyFrom, Currency currencyTo);
    ExchangeRate getBestBuyRateForMonth(Currency currencyFrom, Currency currencyTo);
    int getId();
    String getName();
}
