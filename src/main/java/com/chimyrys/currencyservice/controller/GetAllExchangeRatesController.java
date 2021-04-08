package com.chimyrys.currencyservice.controller;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.ExchangeRate;
import com.chimyrys.currencyservice.service.api.CurrencyService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
public class GetAllExchangeRatesController {
    private final List<CurrencyService> currencyServices;
    @Value("${controller.numberOfThreads}")
    private int numberOfThreads;
    private final static Logger logger = Logger.getLogger(GetAllExchangeRatesController.class);
    private final DateTimeFormatter dateTimeFormatter;

    public GetAllExchangeRatesController(List<CurrencyService> currencyServices) {
        this.currencyServices = currencyServices;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    @RequestMapping(value="/getAllExchangeRates")
    @GetMapping
    public ResponseEntity<List<ExchangeRate>> getAllExchangeRates(@RequestParam String currencyFrom,
                                                                  @RequestParam String currencyTo,
                                                                  @RequestParam String date) {
        logger.info("Getting all exchange rates from all banks");
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        ExecutorCompletionService<Optional<ExchangeRate>> completionService = new ExecutorCompletionService<>(executorService);
        List<ExchangeRate> list = new ArrayList<>();
        List<Future<Optional<ExchangeRate>>> futureList = new ArrayList<>();
        for (CurrencyService currencyService: currencyServices) {
            futureList.add(completionService.submit(
                    () -> currencyService.getCurrency(LocalDate.parse(date, dateTimeFormatter),
                            Currency.valueOf(currencyFrom), Currency.valueOf(currencyTo))
            ));
        }
        for(Future<Optional<ExchangeRate>> future: futureList) {
            try {
                Optional<ExchangeRate> exchangeRate = future.get();
                exchangeRate.ifPresent(list::add);
            } catch (InterruptedException | ExecutionException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
