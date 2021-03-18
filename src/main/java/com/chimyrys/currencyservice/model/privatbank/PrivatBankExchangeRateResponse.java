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

    /**
     * Method getDate() parse string of date from PB json
     * from "d.m.yyyy" and returns it in format "yyyy.m.d"
     * @return String date
     */
    public String getDate() {
        String[] values = date.split("\\.");
        int year = Integer.parseInt(values[2]);
        int month = Integer.parseInt(values[1]);
        int day = Integer.parseInt(values[0]);
        return year + "." + month + "." + day;
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
