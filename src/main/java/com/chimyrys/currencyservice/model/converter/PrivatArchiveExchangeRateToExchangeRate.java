package com.chimyrys.currencyservice.model.converter;

import com.chimyrys.currencyservice.model.ExchangeRate;
import com.chimyrys.currencyservice.model.privatbank.PrivateArchiveExchangeRate;
import com.chimyrys.currencyservice.model.RateDate;
import org.springframework.core.convert.converter.Converter;

/**
 * Class that converts PrivatArchiveExchangeRate objects to ExchangeRate
 */
public class PrivatArchiveExchangeRateToExchangeRate implements Converter<PrivateArchiveExchangeRate, ExchangeRate> {
    @Override
    public ExchangeRate convert(PrivateArchiveExchangeRate privateArchiveExchangeRate) {
        return new ExchangeRate(privateArchiveExchangeRate.getSellPrice(),
                privateArchiveExchangeRate.getBuyPrice(),
                RateDate.createDateFromMillis(privateArchiveExchangeRate.getDate()));
    }
}
