package com.chimyrys.currencyservice.service.api;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.Date;
import com.chimyrys.currencyservice.model.ExchangeRate;

public interface CurrencyService {
    public ExchangeRate getCurrency(Date date, Currency currency);
    public ExchangeRate getBestCurrency(Date date, Currency currency);
    public ExchangeRate getBestCurrencyForWeek(Currency currency);
    public int getId();
}
