package com.chimyrys.currencyservice.service.impl;

import com.chimyrys.currencyservice.model.*;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Service that give us functionality of PrivatBank API for:
 * 1) getting converted currency for specific day
 * 2) getting best (with minimum buy rate) currency for month
 */
@Service
@PropertySource("classpath:/application.properties")
public class PrivatbankCurrencyService implements CurrencyService {
    @Value(value = "${privat.id}")
    private int id;
    @Value(value = "${privat.name}")
    private String name;
    @Value(value = "${privat.url}")
    private String url;
    @Value(value = "${privat.url_monthly}")
    private String url_monthly;
    private final ConversionService conversionService;

    public PrivatbankCurrencyService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public ExchangeRate getCurrency(RateDate rateDate, Currency currencyFrom, Currency currencyTo) {
        String response = getResponseBodyFromBank(rateDate);
        PrivatBankExchangeRateResponse privatBankExchangeRateResponse = conversionService.convert(response, PrivatBankExchangeRateResponse.class);
        if (privatBankExchangeRateResponse == null) {
            return null;
        }

        try {
            List<ExchangeRate> privatbankExchangeRateList = conversionService.convert(privatBankExchangeRateResponse, List.class);
            return privatbankExchangeRateList.stream()
                    .filter(privatbankExchangeRate -> privatbankExchangeRate.getDateTime().equals(rateDate))
                    .filter(privatbankExchangeRate -> privatbankExchangeRate.getCurrencyFrom().equals(currencyFrom))
                    .filter(privatbankExchangeRate -> privatbankExchangeRate.getCurrencyTo().equals(currencyTo))
                    .iterator().next();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public ExchangeRate getBestBuyRateForMonth(Currency currencyFrom, Currency currencyTo) {
        String response = getResponseBodyFromBankMonthly(currencyFrom, currencyTo);
        PrivatbankArchiveExchangeRateResponse privatbankArchiveExchangeRateResponse = conversionService.convert(response, PrivatbankArchiveExchangeRateResponse.class);
        if (privatbankArchiveExchangeRateResponse == null) {
            return null;
        }
        try {
            return privatbankArchiveExchangeRateResponse.getPrivateArchiveExchangeRateList().stream()
                    .map(privateArchiveExchangeRate -> conversionService.convert(privateArchiveExchangeRate, ExchangeRate.class))
                    .filter(Objects::nonNull)
                    .peek(exchangeRate -> exchangeRate.setCurrencyFrom(currencyFrom))
                    .peek(exchangeRate -> exchangeRate.setCurrencyTo(currencyTo))
                    .filter(exchangeRate -> currencyTo.equals(exchangeRate.getCurrencyTo()))
                    .filter(exchangeRate -> currencyFrom.equals(exchangeRate.getCurrencyFrom()))
                    .min((exchangeRate1, exchangeRate2) -> Float.compare(exchangeRate1.getBuyRate(), exchangeRate2.getBuyRate()))
                    .orElseThrow();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public int getId() {
        return id;
    }

    private String getResponseBodyFromBank(RateDate rateDate) {
        HttpClient httpClient = HttpClients.createDefault();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("json")
                .queryParam("date", rateDate.getDay() + "." + rateDate.getMonth() + "." + rateDate.getYear());
        HttpGet httpGet = new HttpGet(builder.build().encode().toUriString());
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

    private String getResponseBodyFromBankMonthly(Currency currencyFrom, Currency currencyTo) {
        HttpClient httpClient = HttpClients.createDefault();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url_monthly)
                .queryParam("period", "month")
                .queryParam("from_currency", Currency.getValueFromId(currencyFrom.getId()))
                .queryParam("to_currency", Currency.getValueFromId(currencyTo.getId()));
        HttpGet httpGet = new HttpGet(builder.build().encode().toUriString());
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
    public String getName() {
        return name;
    }
}
