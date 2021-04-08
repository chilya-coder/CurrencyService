package com.chimyrys.currencyservice.model.converter;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.ExchangeRate;
import com.chimyrys.currencyservice.model.privatbank.PrivatBankExchangeRateResponse;
import com.chimyrys.currencyservice.model.privatbank.PrivatbankExchangeRate;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PrivatExchangeRateToExchangeRate implements Converter<PrivatBankExchangeRateResponse, List<ExchangeRate>> {
    private final DateTimeFormatter dateTimeFormatter;

    public PrivatExchangeRateToExchangeRate() {
        dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    }

    @Override
    public List<ExchangeRate> convert(PrivatBankExchangeRateResponse privatBankExchangeRateResponse) {
        List<ExchangeRate> list = new ArrayList<>();
        Currency baseCurrency = Currency.valueOf(privatBankExchangeRateResponse.getBaseCurrencyLit());
        LocalDate date = LocalDate.parse(privatBankExchangeRateResponse.getDate(), dateTimeFormatter);
        for (PrivatbankExchangeRate privatbankExchangeRate: privatBankExchangeRateResponse.getExchangeRate()) {
            if (privatbankExchangeRate.getCurrency() == null) {
                continue;
            }
            try {
                Currency currency = Currency.valueOf(privatbankExchangeRate.getCurrency());
                list.add(new ExchangeRate(currency, baseCurrency,
                        privatbankExchangeRate.getSaleRate(),
                        privatbankExchangeRate.getPurchaseRate(),
                        date));
            } catch (IllegalArgumentException ignored) {
            }
        }
        return list;
    }
}
