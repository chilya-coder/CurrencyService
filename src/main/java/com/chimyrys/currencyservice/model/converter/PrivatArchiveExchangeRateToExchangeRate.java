package com.chimyrys.currencyservice.model.converter;

import com.chimyrys.currencyservice.model.ExchangeRate;
import com.chimyrys.currencyservice.model.privatbank.PrivateArchiveExchangeRate;
import org.springframework.core.convert.converter.Converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Class that converts PrivatArchiveExchangeRate objects to ExchangeRate
 */
public class PrivatArchiveExchangeRateToExchangeRate implements Converter<PrivateArchiveExchangeRate, ExchangeRate> {
    @Override
    public ExchangeRate convert(PrivateArchiveExchangeRate privateArchiveExchangeRate) {
        return new ExchangeRate(privateArchiveExchangeRate.getSellPrice(),
                privateArchiveExchangeRate.getBuyPrice(),
                LocalDateTime.ofInstant(Instant.ofEpochMilli(privateArchiveExchangeRate.getDate()), ZoneId.systemDefault()));
    }
}
