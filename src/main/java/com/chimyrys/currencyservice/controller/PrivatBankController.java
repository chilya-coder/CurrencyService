package com.chimyrys.currencyservice.controller;

import com.chimyrys.currencyservice.service.api.CurrencyService;
import com.chimyrys.currencyservice.service.api.SaveInfoService;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/privatbank")
public class PrivatBankController extends Controller {
    private final static Logger logger = Logger.getLogger(PrivatBankController.class);

    public PrivatBankController(CurrencyService privatbankCurrencyService, SaveInfoService saveInfoService) {
        super(saveInfoService);
        this.currencyService = privatbankCurrencyService;
    }
}

