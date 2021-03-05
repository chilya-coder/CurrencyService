package com.chimyrys.currencyservice.service.api;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.RateDate;
import com.chimyrys.currencyservice.model.ExchangeRate;

/**
 * Interface for all banks services
 */
public interface CurrencyService {
    ExchangeRate getCurrency(RateDate rateDate, Currency currencyFrom, Currency currencyTo);
    ExchangeRate getBestBuyRateForMonth(Currency currencyFrom, Currency currencyTo);
    int getId();
    String getName();
}
