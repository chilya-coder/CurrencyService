package com.chimyrys.currencyservice.model.converter;

import com.chimyrys.currencyservice.model.ExchangeRate;
import com.chimyrys.currencyservice.model.privatbank.PrivateArchiveExchangeRate;
import org.springframework.core.convert.converter.Converter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Class that converts PrivatArchiveExchangeRate objects to ExchangeRate
 */
public class PrivatArchiveExchangeRateToExchangeRate implements Converter<PrivateArchiveExchangeRate, ExchangeRate> {
    @Override
    public ExchangeRate convert(PrivateArchiveExchangeRate privateArchiveExchangeRate) {
        return new ExchangeRate(privateArchiveExchangeRate.getSellPrice(),
                privateArchiveExchangeRate.getBuyPrice(),
                LocalDate.ofInstant(Instant.ofEpochMilli(privateArchiveExchangeRate.getDate()), ZoneId.systemDefault()));
    }
}
