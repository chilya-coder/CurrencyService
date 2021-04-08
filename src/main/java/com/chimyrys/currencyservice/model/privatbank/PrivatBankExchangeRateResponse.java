package com.chimyrys.currencyservice.model.privatbank;

import java.util.List;

/**
 * Class that represents model of PB object from json response
 */
public class PrivatBankExchangeRateResponse {
    private String date;
    private String bank;
    private int baseCurrency;
    private String baseCurrencyLit;
    private List<PrivatbankExchangeRate> exchangeRate;

    public PrivatBankExchangeRateResponse() {
    }

    public PrivatBankExchangeRateResponse(String date, String bank, int baseCurrency, String baseCurrencyLit, List<PrivatbankExchangeRate> exchangeRate) {
        this.date = date;
        this.bank = bank;
        this.baseCurrency = baseCurrency;
        this.baseCurrencyLit = baseCurrencyLit;
        this.exchangeRate = exchangeRate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public int getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(int baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getBaseCurrencyLit() {
        return baseCurrencyLit;
    }

    public void setBaseCurrencyLit(String baseCurrencyLit) {
        this.baseCurrencyLit = baseCurrencyLit;
    }

    public List<PrivatbankExchangeRate> getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(List<PrivatbankExchangeRate> exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
