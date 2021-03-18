package com.chimyrys.currencyservice.controller;

import com.chimyrys.currencyservice.model.ExchangeRate;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class Controller {

    public abstract ResponseEntity<ExchangeRate> getCurrencyFrom(@RequestParam String date,
                                                                  @RequestParam String currencyFrom,
                                                                  @RequestParam String currencyTo);

    public abstract ExchangeRate bestCurrencyMonth(@RequestParam String currencyFrom,
                                                  @RequestParam String currencyTo);

    public abstract ResponseEntity<InputStreamResource> getDoc(@RequestParam String currencyFrom,
                                                               @RequestParam String currencyTo,
                                                               @RequestParam String date);

}
