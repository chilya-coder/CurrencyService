package com.chimyrys.currencyservice.service.impl;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.ExchangeRate;
import com.chimyrys.currencyservice.model.MonoBankExchangeRateResponse;
import com.chimyrys.currencyservice.model.RateDate;
import com.chimyrys.currencyservice.service.api.CurrencyService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.io.IOException;geimport java.time.Instant;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Service impl that manages:
 * - getting currency A to B for specific date
 * - ....
 */
@Service
public class MonobankCurrencyService implements CurrencyService {
    private int id;
    private String key;
    private String url;
    private ConversionService conversionService;

    public MonobankCurrencyService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public ExchangeRate getCurrency(RateDate rateDate, Currency currencyFrom, Currency currencyTo) {
        String response = getResponseBodyFromBank();
        MonoBankExchangeRateResponse monoBankExchangeRateResponse = conversionService.convert(response, MonoBankExchangeRateResponse.class);
        if (monoBankExchangeRateResponse == null) {
            return null;
        }
            try {
                return Stream.of(monoBankExchangeRateResponse.getMonobankExchangeRateList())
                        .map(monobankExchangeRates -> conversionService.convert(monobankExchangeRates, ExchangeRate.class))
                        .filter(Objects::nonNull)
                        .filter(exchangeRate -> exchangeRate.getDateTime().equals(rateDate))
                        .filter(exchangeRate -> exchangeRate.getCurrencyTo().equals(currencyTo))
                        .filter(exchangeRate -> exchangeRate.getCurrencyFrom().equals(currencyFrom))
                        .iterator().next();
            } catch (NoSuchElementException e) {
                return null;
            }
    }

    @Override
    public ExchangeRate getBestCurrencyForMonth(Currency currencyFrom, Currency currencyTo) {
        String response = getResponseBodyFromBank();
        MonoBankExchangeRateResponse monoBankExchangeRateResponse = conversionService.convert(response, MonoBankExchangeRateResponse.class);
        if (monoBankExchangeRateResponse == null) {
            return null;
        }
        RateDate currentRateDate = RateDate.setDate(Instant.now().getEpochSecond());
        System.out.println(currentRateDate);
       /* try {
            return Stream.of(monoBankExchangeRateResponse.getMonobankExchangeRateList())
                    .map(monobankExchangeRates -> conversionService.convert(monobankExchangeRates, ExchangeRate.class))
                    .filter(Objects::nonNull)
                    .filter(exchangeRate -> exchangeRate.ge)
        } catch (NoSuchElementException e) {
            return null;
        }*/
        return null;
    }

    @Override
    public ExchangeRate getBestCurrencyForWeek(Currency currencyFrom, Currency currencyTo) {
        return null;
    }

    private String getResponseBodyFromBank() {
        String http = "https://api.monobank.ua/bank/currency";
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(http);
        HttpResponse httpResponse = null;
        String json = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            json =  EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public int getId() {
        return 0;
    }
}
