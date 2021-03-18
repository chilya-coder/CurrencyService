package com.chimyrys.currencyservice.model.monobank;

import java.util.List;

/**
 * Class that have List of MonobankExchangeRate Objects
 * got from Monobank API (JSON file)
 */
public class MonoBankExchangeRateResponse {
    private List<MonobankExchangeRate> monobankExchangeRateList;

    public MonoBankExchangeRateResponse(List<MonobankExchangeRate> monobankExchangeRateList) {
        this.monobankExchangeRateList = monobankExchangeRateList;
    }

    public List<MonobankExchangeRate> getMonobankExchangeRateList() {
        return monobankExchangeRateList;
    }

    public void setMonobankExchangeRateList(List<MonobankExchangeRate> monobankExchangeRateList) {
        this.monobankExchangeRateList = monobankExchangeRateList;
    }
}
