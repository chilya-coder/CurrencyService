package com.chimyrys.currencyservice.service.impl;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.Date;
import com.chimyrys.currencyservice.model.ExchangeRate;
import com.chimyrys.currencyservice.service.api.CurrencyService;
import org.springframework.stereotype.Service;

@Service
public class MonobankCurrencyService implements CurrencyService {
    private int id;
    private String key;
    private String url;

    @Override
    public ExchangeRate getCurrency(Date date, Currency currency) {
        return null;
    }

    @Override
    public ExchangeRate getBestCurrency(Date date, Currency currency) {
        return null;
    }

    @Override
    public ExchangeRate getBestCurrencyForWeek(Currency currency) {
        return null;
    }

    @Override
    public int getId() {
        return 0;
    }
}
