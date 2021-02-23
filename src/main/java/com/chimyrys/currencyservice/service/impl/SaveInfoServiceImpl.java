package com.chimyrys.currencyservice.service.impl;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.service.api.SaveInfoService;
import org.springframework.stereotype.Service;

import java.io.File;
@Service
public class SaveInfoServiceImpl implements SaveInfoService {
    @Override
    public File saveBestExchangeRate(String date, Currency currency, int currencyServiceId) {
        return null;
    }
}
