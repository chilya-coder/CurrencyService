package com.chimyrys.currencyservice.controller;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.RateDate;
import com.chimyrys.currencyservice.model.ExchangeRate;
import com.chimyrys.currencyservice.service.api.CurrencyService;
import com.chimyrys.currencyservice.service.api.SaveInfoService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

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
    public ResponseEntity<ExchangeRate> getCurrencyFromPrivatBank(@RequestParam String date,
                                                                  @RequestParam String currencyFrom,
                                                                  @RequestParam String currencyTo) {
        ExchangeRate privatResponse = privatbankCurrencyService
                .getCurrency(new RateDate(date), Currency.valueOf(currencyFrom), Currency.valueOf(currencyTo));
        if (privatResponse != null) {
            return  new ResponseEntity<>(privatResponse, HttpStatus.OK);
        } else {
            return  new ResponseEntity<>(privatResponse, HttpStatus.NOT_FOUND);
        }
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
    @RequestMapping(value="/monobank/getbestcurrency/month")
    @GetMapping
    public ExchangeRate bestCurrencyMonthMonoBank(@RequestParam String currencyFrom,
                                                  @RequestParam String currencyTo) {
        ExchangeRate monoResponse = monobankCurrencyService.getBestBuyRateForMonth(Currency.valueOf(currencyFrom), Currency.valueOf(currencyTo));
        return monoResponse;
    }
    @RequestMapping(value="/privatbank/getbestcurrency/month")
    @GetMapping
    public ExchangeRate bestCurrencyMonthPrivatBank(@RequestParam String currencyFrom,
                                                  @RequestParam String currencyTo) {
        ExchangeRate privatResponse = privatbankCurrencyService.getBestBuyRateForMonth(Currency.valueOf(currencyFrom), Currency.valueOf(currencyTo));
        return privatResponse;
    }
    @RequestMapping(value="/saveExchangeRateToDoc")
    @GetMapping
    public ResponseEntity<InputStreamResource> getDoc(@RequestParam String currencyFrom,
                                                      @RequestParam String currencyTo,
                                                      @RequestParam String date,
                                                      @RequestParam String api_id) {
        byte[] doc = saveInfoService.saveExchangeRate(Currency.valueOf(currencyFrom), Currency.valueOf(currencyTo),
                new RateDate(date), Integer.parseInt(api_id));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "ExchangeRateWordFile.docx");
        headers.setContentType(new MediaType("application", "vnd.openxmlformats-officedocument.wordprocessingml.document"));
        headers.setContentLength(doc.length);
        InputStreamResource inputStreamResource = new InputStreamResource
                (new ByteArrayInputStream(doc));
        return ResponseEntity.ok()
                .headers(headers)
                .body(inputStreamResource);
    }

}
