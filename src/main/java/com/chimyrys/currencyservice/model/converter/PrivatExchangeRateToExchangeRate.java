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
            if (privatbankExchangeRate.getCurrency() == null) {
                continue;
            }
            try {
                Currency currency = Currency.valueOf(privatbankExchangeRate.getCurrency());
                list.add(new ExchangeRate(currency,baseCurrency,
                        privatbankExchangeRate.getSaleRate(),
                        privatbankExchangeRate.getPurchaseRate(),
                        rateDate));
            } catch (IllegalArgumentException i) {
            }

        }
        return list;
    }
}
