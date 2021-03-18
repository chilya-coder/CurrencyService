package com.chimyrys.currencyservice.service.api;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.RateDate;

/**
 * Interface for saving doc files
 */
public interface SaveInfoService {
    /**
     * Method
     * @param currencyFrom
     * @param currencyTo
     * @param rateDate
     * @return
     */
    byte[] saveExchangeRate(Currency currencyFrom, Currency currencyTo, RateDate rateDate, CurrencyService currencyService);
}
