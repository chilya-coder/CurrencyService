package com.chimyrys.currencyservice.model.converter;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.ExchangeRate;
import com.chimyrys.currencyservice.model.MonobankExchangeRate;
import com.chimyrys.currencyservice.model.RateDate;
import org.springframework.core.convert.converter.Converter;

/**
 * Class that uses spring Converter to convert MonobankExchangeRate to ExchangeRate
 */
public class MonoExchangeRateToExchangeRate implements Converter<MonobankExchangeRate, ExchangeRate> {

    @Override
    public ExchangeRate convert(MonobankExchangeRate monobankExchangeRate) {
        return new ExchangeRate(Currency.DEFAULT_INSTANCE.getValueFromId(monobankExchangeRate.getCurrencyCodeA()),
                Currency.DEFAULT_INSTANCE.getValueFromId(monobankExchangeRate.getCurrencyCodeB()),
                monobankExchangeRate.getRateSell(),
                monobankExchangeRate.getRateBuy(),
                RateDate.setDate(monobankExchangeRate.getDate()));
    }
}
