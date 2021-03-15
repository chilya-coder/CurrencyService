package com.chimyrys.currencyservice.controller;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.ExchangeRate;
import com.chimyrys.currencyservice.model.RateDate;
import com.chimyrys.currencyservice.service.api.CurrencyService;
import com.chimyrys.currencyservice.service.api.SaveInfoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@RestController
public class MainController {
    private final CurrencyService monobankCurrencyService;
    private final CurrencyService privatbankCurrencyService;
    private final SaveInfoService saveInfoService;
    private final List<CurrencyService> currencyServices;
    @Value("${controller.numberOfThreads}")
    private int numberOfThreads;
    private final static Logger logger = Logger.getLogger(MainController.class);


    public MainController(CurrencyService monobankCurrencyService, CurrencyService privatbankCurrencyService, SaveInfoService saveInfoService, List<CurrencyService> currencyServices) {
        this.monobankCurrencyService = monobankCurrencyService;
        this.privatbankCurrencyService = privatbankCurrencyService;
        this.saveInfoService = saveInfoService;
        this.currencyServices = currencyServices;
    }


    @RequestMapping("/privatbank/getcurrency")
    @GetMapping
    public ResponseEntity<ExchangeRate> getCurrencyFromPrivatBank(@RequestParam String date,
                                                                  @RequestParam String currencyFrom,
                                                                  @RequestParam String currencyTo) {
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

    @RequestMapping("/monobank/getcurrency")
    @GetMapping
    public ResponseEntity<ExchangeRate> getCurrencyFromMonoBank(@RequestParam String date,
                                                               @RequestParam String currencyFrom,
                                                               @RequestParam String currencyTo) {
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
    @RequestMapping(value="/monobank/getbestcurrency/month")
    @GetMapping
    public ExchangeRate bestCurrencyMonthMonoBank(@RequestParam String currencyFrom,
                                                  @RequestParam String currencyTo) {
        logger.info("Getting currency from MonoBank for month:"
                + "currencyFrom=" + currencyFrom + "," + " currencyTo=" + currencyTo);
        return monobankCurrencyService.getBestBuyRateForMonth(Currency.valueOf(currencyFrom), Currency.valueOf(currencyTo));
    }
    @RequestMapping(value="/privatbank/getbestcurrency/month")
    @GetMapping
    public ExchangeRate bestCurrencyMonthPrivatBank(@RequestParam String currencyFrom,
                                                  @RequestParam String currencyTo) {
        logger.info("Getting currency from PribatBank for month:"
                + "currencyFrom=" + currencyFrom + "," + " currencyTo=" + currencyTo);
        return privatbankCurrencyService.getBestBuyRateForMonth(Currency.valueOf(currencyFrom), Currency.valueOf(currencyTo));
    }
    @RequestMapping(value="/saveExchangeRateToDoc")
    @GetMapping
    public ResponseEntity<InputStreamResource> getDoc(@RequestParam String currencyFrom,
                                                      @RequestParam String currencyTo,
                                                      @RequestParam String date,
                                                      @RequestParam String api_id) {
        logger.info("Saving info to doc file");
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
    @RequestMapping(value="/getAllExchangeRates")
    @GetMapping
    public ResponseEntity<List<ExchangeRate>> getAllExchangeRates(@RequestParam String currencyFrom,
                                                      @RequestParam String currencyTo,
                                                      @RequestParam String date) {
        logger.info("Getting all exchange rates from all banks");
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        ExecutorCompletionService<ExchangeRate> completionService = new ExecutorCompletionService<>(executorService);
        List<ExchangeRate> list = new ArrayList<>();
        for (CurrencyService currencyService: currencyServices) {
            Future<ExchangeRate> submit = null;
            try {
                submit = completionService.submit(() -> currencyService.getCurrency(new RateDate(date), Currency.valueOf(currencyFrom), Currency.valueOf(currencyTo)));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            try {
                assert submit != null;
                list.add(submit.get());
            } catch (InterruptedException | ExecutionException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
