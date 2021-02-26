package com.chimyrys.currencyservice.service.api;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.RateDate;
import com.chimyrys.currencyservice.model.ExchangeRate;

public interface CurrencyService {
    public ExchangeRate getCurrency(RateDate rateDate, Currency currencyFrom, Currency currencyTo);
    public ExchangeRate getBestCurrency(RateDate rateDate, Currency currencyFrom, Currency currencyTo);
    public ExchangeRate getBestCurrencyForWeek(Currency currencyFrom, Currency currencyTo);
    public int getId();
}
