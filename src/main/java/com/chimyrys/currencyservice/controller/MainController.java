package com.chimyrys.currencyservice.controller;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.RateDate;
import com.chimyrys.currencyservice.model.ExchangeRate;
import com.chimyrys.currencyservice.service.api.CurrencyService;
import com.chimyrys.currencyservice.service.api.SaveInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public void getCurrencyFromPrivatBank(String date, Currency currencyFrom, Currency currencyTo) {
        privatbankCurrencyService.getCurrency(new RateDate(date), currencyFrom, currencyTo);
    }

    @RequestMapping("/monobank/getcurrency")
    @GetMapping
    public ExchangeRate getCurrencyFromMonoBank(@RequestParam String date,
                                        @RequestParam String currencyFrom,
                                        @RequestParam String currencyTo) {
        return monobankCurrencyService.getCurrency(new RateDate(date), Currency.valueOf(currencyFrom), Currency.valueOf(currencyTo));
    }
    @RequestMapping(value="/qwerty", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ExchangeRate foo() {
        return new ExchangeRate(Currency.UAH, Currency.USD, 25, 26, new RateDate("2020.01.01"));
    }

}
