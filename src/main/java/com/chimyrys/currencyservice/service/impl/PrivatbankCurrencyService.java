package com.chimyrys.currencyservice.service.impl;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.Date;
import com.chimyrys.currencyservice.model.ExchangeRate;
import com.chimyrys.currencyservice.service.api.CurrencyService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PrivatbankCurrencyService implements CurrencyService {
    private int id;
    private String key;
    private String url;
    @Override
    public ExchangeRate getCurrency(Date date, Currency currency) {

        String http = "https://api.privatbank.ua/p24api/exchange_rates?json&date=" + date.getDay()
                + "." + date.getMonth() + "." + date.getYear();
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
    public ExchangeRate getBestCurrency(Date date, Currency currency) {
        return null;
    }

    @Override
    public ExchangeRate getBestCurrencyForWeek(Currency currency) {
        return null;
    }

    @Override
    public int getId() {
        return 0;
    }
}
