package com.chimyrys.currencyservice.service.api;

import com.chimyrys.currencyservice.model.Currency;

import java.io.File;

public interface SaveInfoService {
    File saveBestExchangeRate(String date, Currency currency, int currencyServiceId);
}
