package com.chimyrys.currencyservice.model.converter;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.ExchangeRate;
import com.chimyrys.currencyservice.model.PrivateArchiveExchangeRate;
import com.chimyrys.currencyservice.model.RateDate;
import org.springframework.core.convert.converter.Converter;

/**
 * Class that converts PrivatArchiveExchangeRate objects to ExchangeRate
 */
public class PrivatArchiveExchangeRateToExchangeRate implements Converter<PrivateArchiveExchangeRate, ExchangeRate> {
    private Currency currencyFrom;
    private Currency currencyTo;

    public Currency getCurrencyFrom() {
        return currencyFrom;
    }

    public void setCurrencyFrom(Currency currencyFrom) {
        this.currencyFrom = currencyFrom;
    }

    public Currency getCurrencyTo() {
        return currencyTo;
    }

    public void setCurrencyTo(Currency currencyTo) {
        this.currencyTo = currencyTo;
    }

    @Override
    public ExchangeRate convert(PrivateArchiveExchangeRate privateArchiveExchangeRate) {
        return new ExchangeRate(privateArchiveExchangeRate.getSellPrice(),
                privateArchiveExchangeRate.getBuyPrice(),
                RateDate.setDate(privateArchiveExchangeRate.getDate()));
    }
}
