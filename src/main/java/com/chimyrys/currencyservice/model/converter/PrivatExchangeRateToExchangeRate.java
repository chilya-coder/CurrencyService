package com.chimyrys.currencyservice.model.converter;

import com.chimyrys.currencyservice.model.*;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.List;

public class PrivatExchangeRateToExchangeRate implements Converter<PrivatBankExchangeRateResponse, List<ExchangeRate>> {

    @Override
    public List<ExchangeRate> convert(PrivatBankExchangeRateResponse privatBankExchangeRateResponse) {
        List<ExchangeRate> list = new ArrayList<>();
        Currency baseCurrency = Currency.valueOf(privatBankExchangeRateResponse.getBaseCurrencyLit());
        RateDate rateDate = new RateDate(privatBankExchangeRateResponse.getDate());
        for (PrivatbankExchangeRate privatbankExchangeRate: privatBankExchangeRateResponse.getExchangeRate()) {
            Currency currency = Currency.valueOf(privatbankExchangeRate.getCurrency());
            list.add(new ExchangeRate(baseCurrency,currency,
                    privatbankExchangeRate.getSaleRate(),
                    privatbankExchangeRate.getPurchaseRate(),
                    rateDate));
        }
        return list;
    }
}
