package com.chimyrys.currencyservice.controller;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.RateDate;
import com.chimyrys.currencyservice.model.ExchangeRate;
import com.chimyrys.currencyservice.service.api.CurrencyService;
import com.chimyrys.currencyservice.service.api.SaveInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;

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
    public ResponseEntity<ExchangeRate> getCurrencyFromMonoBank(@RequestParam String date,
                                                               @RequestParam String currencyFrom,
                                                               @RequestParam String currencyTo) {
        ExchangeRate monoResponse = monobankCurrencyService
                .getCurrency(new RateDate(date), Currency.valueOf(currencyFrom), Currency.valueOf(currencyTo));
        if (monoResponse != null) {
            return  new ResponseEntity<>(monoResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(monoResponse, HttpStatus.NOT_FOUND);
        }
    }
    @RequestMapping(value="/monobank/getbestcurrency/month", method=RequestMethod.GET)
    @GetMapping
    public ExchangeRate smthtotest(@RequestParam String currencyFrom,
                                   @RequestParam String currencyTo) {
        ExchangeRate monoResponse = monobankCurrencyService.getBestCurrencyForMonth(Currency.valueOf(currencyFrom), Currency.valueOf(currencyTo));
        return monoResponse;
    }

}
