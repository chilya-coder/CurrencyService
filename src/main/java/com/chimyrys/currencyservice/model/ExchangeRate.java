package com.chimyrys.currencyservice.model;

/**
 * Class that describes exchange rate for ALL banks
 * with following parameters:
 * currencyFrom - money we convert from
 * currencyTo - money we convert to
 * resultValue - coefficient
 * dateTime - date of actual exchange rate
 */
public class ExchangeRate {
    private Currency currencyFrom; // currency we convert from
    private Currency currencyTo; // currency we convert to
    private float sellRate; // how much currency is worth to buy
    private float buyRate; // how much currency is worth to sell
    private RateDate rateDateTime; //date

    public ExchangeRate(Currency baseCurrency, Currency currency, float sellRate, float buyRate, RateDate rateDateTime) {
        this.currencyFrom = baseCurrency;
        this.currencyTo = currency;
        this.sellRate = sellRate;
        this.buyRate = buyRate;
        this.rateDateTime = rateDateTime;
    }

    public ExchangeRate() {

    }

    public Currency getCurrencyFrom() {
        return currencyFrom;
    }

    public void setCurrencyFrom(Currency currencyFrom) {
        this.currencyFrom = currencyFrom;
    }

    public RateDate getDateTime() {
        return rateDateTime;
    }

    public void setDateTime(RateDate rateDateTime) {
        this.rateDateTime = rateDateTime;
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

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "baseCurrency=" + currencyFrom +
                ", currency=" + currencyTo +
                ", sellRate=" + sellRate +
                ", buyRate=" + buyRate +
                ", rateDateTime=" + rateDateTime +
                '}';
    }
}
