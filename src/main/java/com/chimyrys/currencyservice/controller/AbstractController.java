package com.chimyrys.currencyservice.controller;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.ExchangeRate;
import com.chimyrys.currencyservice.service.api.CurrencyService;
import com.chimyrys.currencyservice.service.api.SaveDataToDocService;
import org.apache.log4j.Logger;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
public abstract class AbstractController {
    protected CurrencyService currencyService;
    private final static Logger logger = Logger.getLogger(AbstractController.class);
    private final SaveDataToDocService saveDataToDocService;
    private final DateTimeFormatter dateTimeFormatter;

    public AbstractController(SaveDataToDocService saveDataToDocService) {
        this.saveDataToDocService = saveDataToDocService;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    /**
     * @param date
     * @param currencyFrom can be any currency from Currency enum list
     * @param currencyTo should be always "UAH"
     * @return
     */
    @RequestMapping("/getcurrency")
    @GetMapping
    public ResponseEntity<ExchangeRate> getCurrencyFrom(@RequestParam String date,
                                                        @RequestParam String currencyFrom,
                                                        @RequestParam String currencyTo) {
        logger.info("Getting currency from " + currencyService.getName() + " for date=" + date + ","
                + " currencyFrom=" + currencyFrom + "," + " currencyTo=" + currencyTo);
        ExchangeRate response = currencyService
                .getCurrency(LocalDate.parse(date, dateTimeFormatter), Currency.valueOf(currencyFrom), Currency.valueOf(currencyTo));
        if (response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * @param currencyFrom can be any currency from Currency enum list
     * @param currencyTo should be always "UAH"
     * @return
     */
    @RequestMapping(value = "/getbestcurrency/month")
    @GetMapping
    public ExchangeRate bestCurrencyMonth(@RequestParam String currencyFrom,
                                          @RequestParam String currencyTo) {
        logger.info("Getting currency from " + currencyService.getName() + "for month:"
                + "currencyFrom=" + currencyFrom + "," + " currencyTo=" + currencyTo);
        return currencyService.getBestBuyRateForMonth(Currency.valueOf(currencyFrom), Currency.valueOf(currencyTo));
    }

    @RequestMapping(value = "/saveExchangeRateToDoc")
    @GetMapping
    public ResponseEntity<InputStreamResource> getDoc(@RequestParam String currencyFrom,
                                                      @RequestParam String currencyTo,
                                                      @RequestParam String date) {
        logger.info("Saving info about" + currencyService.getName() + "to doc file");
        byte[] doc = saveDataToDocService.saveExchangeRate(Currency.valueOf(currencyFrom), Currency.valueOf(currencyTo),
                LocalDate.parse(date, dateTimeFormatter), currencyService);
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
