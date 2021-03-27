package com.chimyrys.currencyservice.controller;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.ExchangeRate;
import com.chimyrys.currencyservice.model.RateDate;
import com.chimyrys.currencyservice.service.api.CurrencyService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@RestController
public class GetAllExchangeRates {
    private final List<CurrencyService> currencyServices;
    @Value("${controller.numberOfThreads}")
    private int numberOfThreads;
    private final static Logger logger = Logger.getLogger(Controller.class);

    public GetAllExchangeRates(List<CurrencyService> currencyServices) {
        this.currencyServices = currencyServices;
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
