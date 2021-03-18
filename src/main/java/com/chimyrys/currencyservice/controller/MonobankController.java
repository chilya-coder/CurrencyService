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
public class MonobankController extends Controller{
    private final CurrencyService monobankCurrencyService;
    private final SaveInfoService saveInfoService;
    private final static Logger logger = Logger.getLogger(MonobankController.class);

    public MonobankController(CurrencyService monobankCurrencyService, SaveInfoService saveInfoService) {
        this.monobankCurrencyService = monobankCurrencyService;
        this.saveInfoService = saveInfoService;
    }

    @RequestMapping("/monobank/getcurrency")
    @GetMapping
    public ResponseEntity<ExchangeRate> getCurrencyFrom(String date, String currencyFrom, String currencyTo) {
        logger.info("Getting currency from MonoBank for date=" + date + ","
                + " currencyFrom=" + currencyFrom + "," + " currencyTo=" + currencyTo);
        ExchangeRate monoResponse = monobankCurrencyService
                .getCurrency(new RateDate(date), Currency.valueOf(currencyFrom), Currency.valueOf(currencyTo));
        if (monoResponse != null) {
            return  new ResponseEntity<>(monoResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(monoResponse, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/monobank/getbestcurrency/month")
    @GetMapping
    @Override
    public ExchangeRate bestCurrencyMonth(String currencyFrom, String currencyTo) {
        logger.info("Getting currency from MonoBank for month:"
                + "currencyFrom=" + currencyFrom + "," + " currencyTo=" + currencyTo);
        return monobankCurrencyService.getBestBuyRateForMonth(Currency.valueOf(currencyFrom), Currency.valueOf(currencyTo));
    }

    @RequestMapping(value="/saveExchangeRateToDoc")
    @GetMapping
    public ResponseEntity<InputStreamResource> getDoc(String currencyFrom, String currencyTo, String date) {
        logger.info("Saving info about monobank to doc file");
        byte[] doc = saveInfoService.saveExchangeRate(Currency.valueOf(currencyFrom), Currency.valueOf(currencyTo),
                new RateDate(date), monobankCurrencyService);
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
