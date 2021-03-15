package com.chimyrys.currencyservice.model.converter;

import com.chimyrys.currencyservice.model.*;
import org.apache.log4j.Logger;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.List;

public class PrivatExchangeRateToExchangeRate implements Converter<PrivatBankExchangeRateResponse, List<ExchangeRate>> {
    private final static Logger logger = Logger.getLogger(PrivatExchangeRateToExchangeRate.class);
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
                logger.error("Can't convert" + PrivatBankExchangeRateResponse.class + " to " + ExchangeRate.class);
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
