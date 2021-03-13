package com.chimyrys.currencyservice.model;

import java.util.List;

/**
 * POJO class that consists of list of PrivateArchiveExchangeRate objects
 */
public class PrivatbankArchiveExchangeRateResponse {
    private List<PrivateArchiveExchangeRate> privateArchiveExchangeRateList;

    public PrivatbankArchiveExchangeRateResponse(List<PrivateArchiveExchangeRate> privateArchiveExchangeRateList) {
        this.privateArchiveExchangeRateList = privateArchiveExchangeRateList;
    }

    public PrivatbankArchiveExchangeRateResponse() {
    }

    public List<PrivateArchiveExchangeRate> getPrivateArchiveExchangeRateList() {
        return privateArchiveExchangeRateList;
    }

    public void setPrivateArchiveExchangeRateList(List<PrivateArchiveExchangeRate> privateArchiveExchangeRateList) {
        this.privateArchiveExchangeRateList = privateArchiveExchangeRateList;
    }
}
