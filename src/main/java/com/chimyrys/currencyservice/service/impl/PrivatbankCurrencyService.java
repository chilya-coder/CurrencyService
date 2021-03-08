package com.chimyrys.currencyservice.service.impl;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.ExchangeRate;
import com.chimyrys.currencyservice.model.PrivatBankExchangeRateResponse;
import com.chimyrys.currencyservice.model.PrivatbankExchangeRate;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@PropertySource("classpath:/application.properties")
public class PrivatbankCurrencyService implements CurrencyService {
    @Value(value = "${privat.id}")
    private int id;
    @Value(value = "${privat.name}")
    private String name;
    @Value(value = "${privat.url}")
    private String url;
    private final ConversionService conversionService;

    public PrivatbankCurrencyService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public ExchangeRate getCurrency(RateDate rateDate, Currency currencyFrom, Currency currencyTo) {
        String response = getResponseBodyFromBank(rateDate);
        System.out.println(response);
        PrivatBankExchangeRateResponse privatBankExchangeRateResponse = conversionService.convert(response, PrivatBankExchangeRateResponse.class);
        if (privatBankExchangeRateResponse == null) {
            return null;
        }
        try {
            List<PrivatbankExchangeRate> privatbankExchangeRateList = conversionService.convert(privatBankExchangeRateResponse, List.class);
        } catch (NoSuchElementException e) {
            return null;
        }
        return null;
    }

    @Override
    public ExchangeRate getBestBuyRateForMonth(Currency currencyFrom, Currency currencyTo) {
        return null;
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

    @Override
    public String getName() {
        return name;
    }
}
