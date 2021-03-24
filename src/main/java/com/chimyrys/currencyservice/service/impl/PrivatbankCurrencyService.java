package com.chimyrys.currencyservice.service.impl;

import com.chimyrys.currencyservice.model.*;
import com.chimyrys.currencyservice.model.converter.JsonToMonoExchangeRateResponse;
import com.chimyrys.currencyservice.model.privatbank.PrivatBankExchangeRateResponse;
import com.chimyrys.currencyservice.model.privatbank.PrivatbankArchiveExchangeRateResponse;
import com.chimyrys.currencyservice.service.api.CurrencyService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Service that give us functionality of PrivatBank API for:
 * 1) getting converted currency for specific day
 * 2) getting best (with minimum buy rate) currency for month
 */
@Service
@PropertySource("classpath:/application.properties")
public class PrivatbankCurrencyService implements CurrencyService {
    private final static Logger logger = Logger.getLogger(PrivatbankCurrencyService.class);
    @Value(value = "${privat.id}")
    private int id;
    @Value(value = "${privat.name}")
    private String name;
    @Value(value = "${privat.url}")
    private String url;
    @Value(value = "${privat.url_monthly}")
    private String url_monthly;
    private final ConversionService conversionService;
    private Environment env;

    public PrivatbankCurrencyService(ConversionService conversionService, Environment env) {
        this.conversionService = conversionService;
        this.env = env;
    }

    @Override
    public ExchangeRate getCurrency(RateDate rateDate, Currency currencyFrom, Currency currencyTo) {
        logger.debug("Getting mono currency with params: " + currencyFrom.getValue() + ", "
                + currencyTo.getValue());
        String response = getResponseBodyFromBank(rateDate);
        logger.debug(env.getProperty("logging.string.covert.json_to_response")
                + PrivatBankExchangeRateResponse.class);
        PrivatBankExchangeRateResponse privatBankExchangeRateResponse = conversionService.convert(response, PrivatBankExchangeRateResponse.class);
        if (privatBankExchangeRateResponse == null) {
            return null;
        }

        try {
            logger.debug("Converting " + PrivatBankExchangeRateResponse.class + " to" +
                    ExchangeRate.class);
            List<ExchangeRate> privatbankExchangeRateList = conversionService.convert(privatBankExchangeRateResponse, List.class);
            return privatbankExchangeRateList.stream()
                    .filter(privatbankExchangeRate -> privatbankExchangeRate.getDateTime().equals(rateDate))
                    .filter(privatbankExchangeRate -> privatbankExchangeRate.getCurrencyFrom().equals(currencyFrom))
                    .filter(privatbankExchangeRate -> privatbankExchangeRate.getCurrencyTo().equals(currencyTo))
                    .peek(exchangeRate -> logger.debug("Result: " + exchangeRate))
                    .iterator().next();
        } catch (NoSuchElementException e) {
            logger.error(env.getProperty("logging.string.no_param") + rateDate.getYear() + "." + rateDate.getMonth()
                    + rateDate.getDay() + ", currencyFrom" + currencyFrom.getValue() + ", currencyTo" + currencyTo);
            return null;
        }
    }

    @Override
    public ExchangeRate getBestBuyRateForMonth(Currency currencyFrom, Currency currencyTo) {
        logger.debug("Getting privat best currency for month with params: " + currencyFrom.getValue() + ", "
                + currencyTo.getValue());
        String response = getResponseBodyFromBankMonthly(currencyFrom, currencyTo);
        logger.debug(env.getProperty("logging.string.covert.json_to_response")
                + PrivatbankArchiveExchangeRateResponse.class);
        PrivatbankArchiveExchangeRateResponse privatbankArchiveExchangeRateResponse = conversionService.convert(response, PrivatbankArchiveExchangeRateResponse.class);
        if (privatbankArchiveExchangeRateResponse == null) {
            return null;
        }
        try {
            logger.debug("Converting " + PrivatbankArchiveExchangeRateResponse.class + " to" +
                    ExchangeRate.class);
            return privatbankArchiveExchangeRateResponse.getPrivateArchiveExchangeRateList().stream()
                    .map(privateArchiveExchangeRate -> conversionService.convert(privateArchiveExchangeRate, ExchangeRate.class))
                    .filter(Objects::nonNull)
                    .peek(exchangeRate -> exchangeRate.setCurrencyFrom(currencyFrom))
                    .peek(exchangeRate -> exchangeRate.setCurrencyTo(currencyTo))
                    .filter(exchangeRate -> currencyTo.equals(exchangeRate.getCurrencyTo()))
                    .filter(exchangeRate -> currencyFrom.equals(exchangeRate.getCurrencyFrom()))
                    .peek(exchangeRate -> logger.debug("Result: " + exchangeRate))
                    .min((exchangeRate1, exchangeRate2) -> Float.compare(exchangeRate1.getBuyRate(), exchangeRate2.getBuyRate()))
                    .orElseThrow();
        } catch (NoSuchElementException e) {
            logger.error(env.getProperty("logging.string.no_param") + currencyFrom.getValue() + ", currencyTo=" + currencyTo.getValue());
            return null;
        }
    }

    @Override
    public int getId() {
        return id;
    }

    private String getResponseBodyFromBank(RateDate rateDate) {
        HttpClient httpClient = HttpClients.createDefault();
        logger.debug("Try to get response about exchange rate from privatbank");
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("json")
                .queryParam("date", rateDate.getDay() + "." + rateDate.getMonth() + "." + rateDate.getYear());
        HttpGet httpGet = new HttpGet(builder.build().encode().toUriString());
        HttpResponse httpResponse = null;
        String json = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            json =  EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            logger.error(env.getProperty("logging.string.no_json"));
            e.printStackTrace();
        }
        return json;
    }

    private String getResponseBodyFromBankMonthly(Currency currencyFrom, Currency currencyTo) {
        HttpClient httpClient = HttpClients.createDefault();
        logger.debug("Try to get response about exchange rate from privatbank");
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url_monthly)
                .queryParam("period", "month")
                .queryParam("from_currency", Currency.getValueFromId(currencyFrom.getId()))
                .queryParam("to_currency", Currency.getValueFromId(currencyTo.getId()));
        HttpGet httpGet = new HttpGet(builder.build().encode().toUriString());
        HttpResponse httpResponse = null;
        String json = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            json =  EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            logger.error(env.getProperty("logging.string.no_json"));
            e.printStackTrace();
        }
        return json;
    }
    @Override
    public String getName() {
        return name;
    }
}
