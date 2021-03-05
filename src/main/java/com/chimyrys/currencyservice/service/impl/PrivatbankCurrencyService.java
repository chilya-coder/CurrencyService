package com.chimyrys.currencyservice.service.impl;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.RateDate;
import com.chimyrys.currencyservice.model.ExchangeRate;
import com.chimyrys.currencyservice.service.api.CurrencyService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@PropertySource("classpath:/application.properties")
public class PrivatbankCurrencyService implements CurrencyService {
    @Value(value = "${privat.id}")
    private int id;
    @Value(value = "${privat.name}")
    private String name;
    @Value(value = "https://api.privatbank.ua/p24api/exchange_rates?json")
    private String url;
    @Override
    public ExchangeRate getCurrency(RateDate rateDate, Currency currencyFrom, Currency currencyTo) {

        String http = "https://api.privatbank.ua/p24api/exchange_rates?json&date=" + rateDate.getDay()
                + "." + rateDate.getMonth() + "." + rateDate.getYear();
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(http);
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            return null;
            //return EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
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

    @Override
    public String getName() {
        return name;
    }
}
