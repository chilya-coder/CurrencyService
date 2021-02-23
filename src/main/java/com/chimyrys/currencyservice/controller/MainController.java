package com.chimyrys.currencyservice.controller;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.Date;
import com.chimyrys.currencyservice.model.ExchangeRate;
import com.chimyrys.currencyservice.service.api.CurrencyService;
import com.chimyrys.currencyservice.service.api.SaveInfoService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
public class MainController {
    private final CurrencyService monobankCurrencyService;
    private CurrencyService privatbankCurrencyService;
    private SaveInfoService saveInfoService;


    public MainController(CurrencyService monobankCurrencyService, CurrencyService privatbankCurrencyService, SaveInfoService saveInfoService) {
        this.monobankCurrencyService = monobankCurrencyService;
        this.privatbankCurrencyService = privatbankCurrencyService;
        this.saveInfoService = saveInfoService;
    }


    @RequestMapping("/privatbank/getcurrency")
    @GetMapping
    public void getCurrencyFromPrivatBank(String date, Currency currency) {
        privatbankCurrencyService.getCurrency(new Date(date), currency);
    }

    @RequestMapping(value="/qwerty", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ExchangeRate foo() {
        return new ExchangeRate(Currency.UAH, Currency.USD, 25, new Date("2020.01.01"));
    }

    @RequestMapping("/monobank/getcurrency")
    @GetMapping
    public String mono() {
        String http = "https://api.monobank.ua/bank/currency";
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(http);
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            return EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "oops";
    }
}
