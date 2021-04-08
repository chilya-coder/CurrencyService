package com.chimyrys.currencyservice.model.converter;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.ExchangeRate;
import com.chimyrys.currencyservice.model.monobank.MonobankExchangeRate;
import org.springframework.core.convert.converter.Converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Class that uses spring Converter to convert MonobankExchangeRate to ExchangeRate
 */
public class MonoExchangeRateToExchangeRate implements Converter<MonobankExchangeRate, ExchangeRate> {

    @Override
    public ExchangeRate convert(MonobankExchangeRate monobankExchangeRate) {
        return new ExchangeRate(Currency.getValueFromId(monobankExchangeRate.getCurrencyCodeA()),
                Currency.getValueFromId(monobankExchangeRate.getCurrencyCodeB()),
                monobankExchangeRate.getRateSell(),
                monobankExchangeRate.getRateBuy(),
                LocalDateTime.ofInstant(Instant.ofEpochSecond(monobankExchangeRate.getDate()), ZoneId.systemDefault()).toLocalDate());
    }
}
