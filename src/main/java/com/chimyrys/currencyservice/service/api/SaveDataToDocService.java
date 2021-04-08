package com.chimyrys.currencyservice.service.api;

import com.chimyrys.currencyservice.model.Currency;

import java.time.LocalDate;
import java.util.Optional;

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
    Optional<byte[]> saveExchangeRateToDoc(Currency currencyFrom, Currency currencyTo, LocalDate date, CurrencyService currencyService);
}
