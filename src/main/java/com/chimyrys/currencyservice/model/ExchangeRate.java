package com.chimyrys.currencyservice.model;

public class ExchangeRate {
    private Currency baseCurrency;
    private Currency currency;
    private float resultValue;
    private Date dateTime;

    public ExchangeRate(Currency baseCurrency, Currency currency, float resultValue, Date dateTime) {
        this.baseCurrency = baseCurrency;
        this.currency = currency;
        this.resultValue = resultValue;
        this.dateTime = dateTime;
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Currency baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public float getResultValue() {
        return resultValue;
    }

    public void setResultValue(float resultValue) {
        this.resultValue = resultValue;
    }
}
