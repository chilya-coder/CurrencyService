package com.chimyrys.currencyservice.service.impl;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.ExchangeRate;
import com.chimyrys.currencyservice.model.monobank.MonoBankExchangeRateResponse;
import com.chimyrys.currencyservice.service.api.CurrencyService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * Service that give us functionality of MonoBank API for:
 * 1) getting converted currency for specific day
 * 2) getting best (with minimum buy rate) currency for month
 */
@Service
public class MonobankCurrencyService implements CurrencyService {
    private final static Logger logger = Logger.getLogger(MonobankCurrencyService.class);
    @Value(value = "${monobank.id}")
    private int id;
    @Value(value = "${monobank.name}")
    private String name;
    @Value(value = "${monobank.url}")
    private String url;
    private final ConversionService conversionService;
    private final Environment env;

    public MonobankCurrencyService(ConversionService conversionService, Environment env) {
        this.conversionService = conversionService;
        this.env = env;
    }

    @Override
    public Optional<ExchangeRate> getCurrency(LocalDate date, Currency currencyFrom, Currency currencyTo) {
        logger.debug("Getting mono currency with params: " + currencyFrom.getValue() + ", " + currencyTo.getValue());
        Optional<MonoBankExchangeRateResponse> monoBankExchangeRateResponse = getMonoBankExhangeRateResponse();
        return monoBankExchangeRateResponse.isEmpty() ? Optional.empty() : monoBankExchangeRateResponse.get()
                .getMonobankExchangeRateList().stream()
                .map(monobankExchangeRates -> conversionService.convert(monobankExchangeRates, ExchangeRate.class))
                .filter(Objects::nonNull)
                .filter(exchangeRate -> exchangeRate.getCurrencyTo() != null)
                .filter(exchangeRate -> exchangeRate.getCurrencyFrom() != null)
                .filter(exchangeRate -> exchangeRate.getDate().equals(date))
                .filter(exchangeRate -> exchangeRate.getCurrencyTo().equals(currencyTo))
                .filter(exchangeRate -> exchangeRate.getCurrencyFrom().equals(currencyFrom))
                .peek(exchangeRate -> logger.debug("Result: " + exchangeRate))
                .findAny();
    }

    @Override
    public Optional<ExchangeRate> getBestBuyRateForMonth(Currency currencyFrom, Currency currencyTo) {
        logger.debug("Getting mono best currency for month with params: " + currencyFrom.getValue() + ", " + currencyTo.getValue());
        Optional<MonoBankExchangeRateResponse> monoBankExchangeRateResponse = getMonoBankExhangeRateResponse();
        return monoBankExchangeRateResponse.isEmpty() ? Optional.empty() : monoBankExchangeRateResponse.get()
                .getMonobankExchangeRateList().stream()
                .map(monobankExchangeRates -> conversionService.convert(monobankExchangeRates, ExchangeRate.class))
                .filter(Objects::nonNull)
                .filter(exchangeRate -> currencyTo.equals(exchangeRate.getCurrencyTo()))
                .filter(exchangeRate -> currencyFrom.equals(exchangeRate.getCurrencyFrom()))
                .filter(exchangeRate -> exchangeRate.getDate().getYear() == LocalDateTime.now().getYear())
                .filter(exchangeRate -> exchangeRate.getDate().getMonth() == LocalDateTime.now().getMonth())
                .peek(exchangeRate -> logger.debug("Result: " + exchangeRate))
                .min((exchangeRate1, exchangeRate2) -> Float.compare(exchangeRate1.getBuyRate(), exchangeRate2.getBuyRate()));
    }

    private Optional<MonoBankExchangeRateResponse> getMonoBankExhangeRateResponse() {
        String response;
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            logger.debug("Try to get response about exchange rate from monobank");
            HttpResponse httpResponse = httpClient.execute(httpGet);
            logger.debug("Response from monobank is got");
            response = EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            logger.error(env.getProperty("logging.string.no_json"));
            return Optional.empty();
        }
        logger.debug(env.getProperty("logging.string.covert.json_to_response")
                + MonoBankExchangeRateResponse.class);
        MonoBankExchangeRateResponse monoBankExchangeRateResponse = conversionService.convert(response, MonoBankExchangeRateResponse.class);
        return Optional.ofNullable(monoBankExchangeRateResponse);
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
