package com.chimyrys.currencyservice.service.api;

import com.chimyrys.currencyservice.model.Currency;

import java.time.LocalDate;

/**
 * Interface for saving doc files
 */
public interface SaveDataToDocService {
    /**
     * Method
     * @param currencyFrom
     * @param currencyTo
     * @param date
     * @return
     */
    byte[] saveExchangeRate(Currency currencyFrom, Currency currencyTo, LocalDate date, CurrencyService currencyService);
}
