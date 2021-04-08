package com.chimyrys.currencyservice.model;

import java.time.LocalDate;

/**
 * Class that describes exchange rate for ALL banks
 * with following parameters:
 * currencyFrom - money we convert from
 * currencyTo - money we convert to
 * resultValue - coefficient
 * date - date of actual exchange rate
 */
public class ExchangeRate {
    private Currency currencyFrom; // currency we convert from
    private Currency currencyTo; // currency we convert to
    private float sellRate; // how much currency is worth to buy
    private float buyRate; // how much currency is worth to sell
    private LocalDate date; //date

    public ExchangeRate(Currency baseCurrency, Currency currency, float sellRate, float buyRate, LocalDate date) {
        this.currencyFrom = baseCurrency;
        this.currencyTo = currency;
        this.sellRate = sellRate;
        this.buyRate = buyRate;
        this.date = date;
    }

    public ExchangeRate(float sellRate, float buyRate, LocalDate date) {
        this.sellRate = sellRate;
        this.buyRate = buyRate;
        this.date = date;
    }

    public ExchangeRate() {

    }

    public Currency getCurrencyFrom() {
        return currencyFrom;
    }

    public void setCurrencyFrom(Currency currencyFrom) {
        this.currencyFrom = currencyFrom;
    }


    public Currency getCurrencyTo() {
        return currencyTo;
    }

    public void setCurrencyTo(Currency currencyTo) {
        this.currencyTo = currencyTo;
    }

    public float getSellRate() {
        return sellRate;
    }

    public void setSellRate(float sellRate) {
        this.sellRate = sellRate;
    }

    public float getBuyRate() {
        return buyRate;
    }

    public void setBuyRate(float buyRate) {
        this.buyRate = buyRate;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "currencyFrom=" + currencyFrom +
                ", currencyTo=" + currencyTo +
                ", sellRate=" + sellRate +
                ", buyRate=" + buyRate +
                ", date=" + date +
                '}';
    }
}
