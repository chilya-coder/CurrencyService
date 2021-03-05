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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Service impl that manages:
 * - getting currency A to B for specific date
 * - ....
 */
@Service
@PropertySource("classpath:/application.properties")
public class MonobankCurrencyService implements CurrencyService {
    @Value(value = "${monobank.id}")
    private int id;
    @Value(value = "${monobank.name}")
    private String name;
    @Value(value = "${monobank.url}")
    private String url;
    private final ConversionService conversionService;

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
                return monoBankExchangeRateResponse.getMonobankExchangeRateList().stream()
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
    public ExchangeRate getBestBuyRateForMonth(Currency currencyFrom, Currency currencyTo) {
        String response = getResponseBodyFromBank();
        MonoBankExchangeRateResponse monoBankExchangeRateResponse = conversionService.convert(response, MonoBankExchangeRateResponse.class);
        if (monoBankExchangeRateResponse == null) {
            return null;
        }
        RateDate currentRateDate = RateDate.setDate(Instant.now().getEpochSecond());
        try {
             return monoBankExchangeRateResponse.getMonobankExchangeRateList().stream()
                     .map(monobankExchangeRates -> conversionService.convert(monobankExchangeRates, ExchangeRate.class))
                     .filter(Objects::nonNull)
                     .filter(exchangeRate -> currencyTo.equals(exchangeRate.getCurrencyTo()))
                     .filter(exchangeRate -> currencyFrom.equals(exchangeRate.getCurrencyFrom()))
                     .filter(exchangeRate -> exchangeRate.getDateTime().getYear() == currentRateDate.getYear())
                     .filter(exchangeRate -> exchangeRate.getDateTime().getMonth() == currentRateDate.getMonth())
                     .min((exchangeRate1, exchangeRate2) -> Float.compare(exchangeRate1.getBuyRate(), exchangeRate2.getBuyRate()))
                     .orElseThrow();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private String getResponseBodyFromBank() {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
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
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
