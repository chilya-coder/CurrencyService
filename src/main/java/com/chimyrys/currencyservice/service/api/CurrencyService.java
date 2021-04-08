package com.chimyrys.currencyservice.service.api;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.ExchangeRate;

import java.time.LocalDateTime;

/**
 * Interface for all banks services
 */
public interface CurrencyService {
    ExchangeRate getCurrency(LocalDateTime date, Currency currencyFrom, Currency currencyTo);
    ExchangeRate getBestBuyRateForMonth(Currency currencyFrom, Currency currencyTo);
    int getId();
    String getName();
}
