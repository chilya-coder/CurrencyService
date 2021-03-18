package com.chimyrys.currencyservice.service.impl;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.ExchangeRate;
import com.chimyrys.currencyservice.model.monobank.MonoBankExchangeRateResponse;
import com.chimyrys.currencyservice.model.RateDate;
import com.chimyrys.currencyservice.model.converter.JsonToMonoExchangeRateResponse;
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

import java.io.IOException;
import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.Objects;

 /**
 * Service that give us functionality of MonoBank API for:
 * 1) getting converted currency for specific day
 * 2) getting best (with minimum buy rate) currency for month
 */
@Service
@PropertySource("classpath:/application.properties")
public class MonobankCurrencyService implements CurrencyService {
    private final static Logger logger = Logger.getLogger(JsonToMonoExchangeRateResponse.class);
    @Value(value = "${monobank.id}")
    private int id;
    @Value(value = "${monobank.name}")
    private String name;
    @Value(value = "${monobank.url}")
    private String url;
    private final ConversionService conversionService;
    private Environment env;

    public MonobankCurrencyService(ConversionService conversionService, Environment env) {
        this.conversionService = conversionService;
        this.env = env;
    }

    @Override
    public ExchangeRate getCurrency(RateDate rateDate, Currency currencyFrom, Currency currencyTo) {
        logger.debug("Getting mono currency with params: " + currencyFrom.getValue() + ", "
                + currencyTo.getValue());
        String response = getResponseBodyFromBank();
        logger.debug(env.getProperty("logging.string.covert.json_to_response")
                + MonoBankExchangeRateResponse.class);
        MonoBankExchangeRateResponse monoBankExchangeRateResponse = conversionService.convert(response, MonoBankExchangeRateResponse.class);
        if (Objects.isNull(monoBankExchangeRateResponse)) {
            logger.error("monoBankExchangeRateResponse is null");
            throw new NullPointerException();
        }
            try {
                logger.debug("Converting " + MonoBankExchangeRateResponse.class + " to" +
                        ExchangeRate.class);
                return monoBankExchangeRateResponse.getMonobankExchangeRateList().stream()
                        .map(monobankExchangeRates -> conversionService.convert(monobankExchangeRates, ExchangeRate.class))
                        .filter(Objects::nonNull)
                        .filter(exchangeRate -> exchangeRate.getCurrencyTo() != null)
                        .filter(exchangeRate -> exchangeRate.getCurrencyFrom() != null)
                        .filter(exchangeRate -> exchangeRate.getDateTime().equals(rateDate))
                        .filter(exchangeRate -> exchangeRate.getCurrencyTo().equals(currencyTo))
                        .filter(exchangeRate -> exchangeRate.getCurrencyFrom().equals(currencyFrom))
                        .peek(exchangeRate -> logger.debug("Result: " + exchangeRate))
                        .iterator().next();
            } catch (NoSuchElementException e) {
                logger.error(env.getProperty("logging.string.no_param") +  "rateDate=" + rateDate.getYear() + "." + rateDate.getMonth() + "."
                + rateDate.getDay() + ", currencyFrom=" + currencyFrom.getValue() + ", currencyTo=" + currencyTo);
                return null;
            }
    }

    @Override
    public ExchangeRate getBestBuyRateForMonth(Currency currencyFrom, Currency currencyTo) {
        logger.debug("Getting mono best currency for month with params: " + currencyFrom.getValue() + ", "
        + currencyTo.getValue());
        String response = getResponseBodyFromBank();
        logger.debug(env.getProperty("logging.string.covert.json_to_response")
                + MonoBankExchangeRateResponse.class);
        MonoBankExchangeRateResponse monoBankExchangeRateResponse = conversionService.convert(response, MonoBankExchangeRateResponse.class);
        if (monoBankExchangeRateResponse == null) {
            return null;
        }
        RateDate currentRateDate = RateDate.createDateFromSeconds(Instant.now().getEpochSecond());
        try {
            logger.debug("Converting " + MonoBankExchangeRateResponse.class + " to" +
                    ExchangeRate.class);
             return monoBankExchangeRateResponse.getMonobankExchangeRateList().stream()
                     .map(monobankExchangeRates -> conversionService.convert(monobankExchangeRates, ExchangeRate.class))
                     .filter(Objects::nonNull)
                     .filter(exchangeRate -> currencyTo.equals(exchangeRate.getCurrencyTo()))
                     .filter(exchangeRate -> currencyFrom.equals(exchangeRate.getCurrencyFrom()))
                     .filter(exchangeRate -> exchangeRate.getDateTime().getYear() == currentRateDate.getYear())
                     .filter(exchangeRate -> exchangeRate.getDateTime().getMonth() == currentRateDate.getMonth())
                     .peek(exchangeRate -> logger.debug("Result: " + exchangeRate))
                     .min((exchangeRate1, exchangeRate2) -> Float.compare(exchangeRate1.getBuyRate(), exchangeRate2.getBuyRate()))
                     .orElseThrow();
        } catch (NoSuchElementException e) {
            logger.error(env.getProperty("logging.string.no_param") + " currencyFrom={}" + currencyFrom.getValue() + ", currencyTo=" + currencyTo.getValue());
            return null;
        }
    }

    private String getResponseBodyFromBank() {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        logger.debug("Try to get response about exchange rate from monobank");
        HttpResponse httpResponse = null;
        String json = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            json =  EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            logger.error(env.getProperty("logging.string.no_json"));
            return null;
        }
        logger.debug("Response from monobank is got");
        return json;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
