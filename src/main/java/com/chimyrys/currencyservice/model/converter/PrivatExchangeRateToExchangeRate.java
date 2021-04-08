package com.chimyrys.currencyservice.model.converter;

import com.chimyrys.currencyservice.model.*;
import com.chimyrys.currencyservice.model.privatbank.PrivatBankExchangeRateResponse;
import com.chimyrys.currencyservice.model.privatbank.PrivatbankExchangeRate;
import org.apache.log4j.Logger;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PrivatExchangeRateToExchangeRate implements Converter<PrivatBankExchangeRateResponse, List<ExchangeRate>> {
    private final static Logger logger = Logger.getLogger(PrivatExchangeRateToExchangeRate.class);
    private final DateTimeFormatter dateTimeFormatter;

    public PrivatExchangeRateToExchangeRate() {
        dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    }

    @Override
    public List<ExchangeRate> convert(PrivatBankExchangeRateResponse privatBankExchangeRateResponse) {
        List<ExchangeRate> list = new ArrayList<>();
        Currency baseCurrency = Currency.valueOf(privatBankExchangeRateResponse.getBaseCurrencyLit());
        LocalDateTime rateDate = LocalDate.parse(privatBankExchangeRateResponse.getDate(), dateTimeFormatter).atStartOfDay();
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
