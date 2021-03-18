package com.chimyrys.currencyservice.controller;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.ExchangeRate;
import com.chimyrys.currencyservice.model.RateDate;
import com.chimyrys.currencyservice.service.api.CurrencyService;
import com.chimyrys.currencyservice.service.api.SaveInfoService;
import org.apache.log4j.Logger;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
public class PrivatBankController extends Controller{
    private final CurrencyService privatbankCurrencyService;
    private final SaveInfoService saveInfoService;
    private final static Logger logger = Logger.getLogger(PrivatBankController.class);

    public PrivatBankController(CurrencyService privatbankCurrencyService, SaveInfoService saveInfoService) {
        this.privatbankCurrencyService = privatbankCurrencyService;
        this.saveInfoService = saveInfoService;
    }

    @RequestMapping("/privatbank/getcurrency")
    @GetMapping
    @Override
    public ResponseEntity<ExchangeRate> getCurrencyFrom(String date, String currencyFrom, String currencyTo) {
        logger.info("Getting currency from PrivatBank for date=" + date + ","
                + " currencyFrom=" + currencyFrom + "," + " currencyTo=" + currencyTo);
        ExchangeRate privatResponse = privatbankCurrencyService
                .getCurrency(new RateDate(date), Currency.valueOf(currencyFrom), Currency.valueOf(currencyTo));
        if (privatResponse != null) {
            return new ResponseEntity<>(privatResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(privatResponse, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value="/privatbank/getbestcurrency/month")
    @GetMapping
    @Override
    public ExchangeRate bestCurrencyMonth(String currencyFrom, String currencyTo) {
        logger.info("Getting currency from PribatBank for month:"
                + "currencyFrom=" + currencyFrom + "," + " currencyTo=" + currencyTo);
        return privatbankCurrencyService.getBestBuyRateForMonth(Currency.valueOf(currencyFrom), Currency.valueOf(currencyTo));
    }

    @Override
    public ResponseEntity<InputStreamResource> getDoc(String currencyFrom, String currencyTo, String date) {
        logger.info("Saving info about privatbank to doc file");
        byte[] doc = saveInfoService.saveExchangeRate(Currency.valueOf(currencyFrom), Currency.valueOf(currencyTo),
                new RateDate(date), privatbankCurrencyService);
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

