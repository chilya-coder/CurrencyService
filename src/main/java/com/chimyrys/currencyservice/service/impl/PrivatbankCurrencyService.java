package com.chimyrys.currencyservice.service.impl;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.ExchangeRate;
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
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Service that give us functionality of PrivatBank API for:
 * 1) getting converted currency for specific day
 * 2) getting best (with minimum buy rate) currency for month
 */
@Service
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
    public Optional<ExchangeRate> getCurrency(LocalDate date, Currency currencyFrom, Currency currencyTo) {
        logger.debug("Getting mono currency with params: " + currencyFrom.getValue() + ", "
                + currencyTo.getValue());
        String response;
        try {
            response = getResponseBodyFromBank(date);
        } catch (IOException e) {
            logger.error(env.getProperty("logging.string.no_json"));
            return Optional.empty();
        }
        logger.debug(env.getProperty("logging.string.covert.json_to_response") + PrivatBankExchangeRateResponse.class);
        PrivatBankExchangeRateResponse privatBankExchangeRateResponse = conversionService.convert(response, PrivatBankExchangeRateResponse.class);
        logger.debug("Converting " + PrivatBankExchangeRateResponse.class + " to" + ExchangeRate.class);
        List<ExchangeRate> privatbankExchangeRateList = conversionService.convert(privatBankExchangeRateResponse, List.class);
        if(privatbankExchangeRateList == null) {
            return Optional.empty();
        }
        return privatbankExchangeRateList.stream()
                .filter(privatbankExchangeRate -> privatbankExchangeRate.getDate().equals(date))
                .filter(privatbankExchangeRate -> privatbankExchangeRate.getCurrencyFrom().equals(currencyFrom))
                .filter(privatbankExchangeRate -> privatbankExchangeRate.getCurrencyTo().equals(currencyTo))
                .peek(exchangeRate -> logger.debug("Result: " + exchangeRate))
                .findAny();
    }

    @Override
    public Optional<ExchangeRate> getBestBuyRateForMonth(Currency currencyFrom, Currency currencyTo) {
        logger.debug("Getting privat best currency for month with params: " + currencyFrom.getValue() + ", "
                + currencyTo.getValue());
        String response;
        try {
            response = getResponseBodyFromBankMonthly(currencyFrom, currencyTo);
        } catch (IOException e) {
            logger.error(env.getProperty("logging.string.no_json"));
            return Optional.empty();
        }
        logger.debug(env.getProperty("logging.string.covert.json_to_response")
                + PrivatbankArchiveExchangeRateResponse.class);
        PrivatbankArchiveExchangeRateResponse privatbankArchiveExchangeRateResponse = conversionService.convert(response, PrivatbankArchiveExchangeRateResponse.class);
        if (privatbankArchiveExchangeRateResponse == null) {
            return Optional.empty();
        }
        logger.debug("Converting " + PrivatbankArchiveExchangeRateResponse.class + " to" + ExchangeRate.class);
        return privatbankArchiveExchangeRateResponse.getPrivateArchiveExchangeRateList().stream()
                .map(privateArchiveExchangeRate -> conversionService.convert(privateArchiveExchangeRate, ExchangeRate.class))
                .filter(Objects::nonNull)
                .peek(exchangeRate -> exchangeRate.setCurrencyFrom(currencyFrom))
                .peek(exchangeRate -> exchangeRate.setCurrencyTo(currencyTo))
                .filter(exchangeRate -> currencyTo.equals(exchangeRate.getCurrencyTo()))
                .filter(exchangeRate -> currencyFrom.equals(exchangeRate.getCurrencyFrom()))
                .peek(exchangeRate -> logger.debug("Result: " + exchangeRate))
                .min((exchangeRate1, exchangeRate2) -> Float.compare(exchangeRate1.getBuyRate(), exchangeRate2.getBuyRate()));
    }

    private String getResponseBodyFromBank(LocalDate date) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        logger.debug("Try to get response about exchange rate from privatbank");
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("json")
                .queryParam("date", date.getDayOfMonth() + "." + date.getMonthValue() + "." + date.getYear());
        HttpGet httpGet = new HttpGet(builder.build().encode().toUriString());
        HttpResponse httpResponse = httpClient.execute(httpGet);
        return EntityUtils.toString(httpResponse.getEntity());
    }

    private String getResponseBodyFromBankMonthly(Currency currencyFrom, Currency currencyTo) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        logger.debug("Try to get response about exchange rate from privatbank");
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url_monthly)
                .queryParam("period", "month")
                .queryParam("from_currency", Currency.getValueFromId(currencyFrom.getId()))
                .queryParam("to_currency", Currency.getValueFromId(currencyTo.getId()));
        HttpGet httpGet = new HttpGet(builder.build().encode().toUriString());
        HttpResponse httpResponse = httpClient.execute(httpGet);
        return EntityUtils.toString(httpResponse.getEntity());
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
