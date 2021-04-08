package com.chimyrys.currencyservice.controller;

import com.chimyrys.currencyservice.service.api.CurrencyService;
import com.chimyrys.currencyservice.service.api.SaveDataToDocService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/privatbank")
public class PrivatBankController extends AbstractController {

    public PrivatBankController(CurrencyService privatbankCurrencyService, SaveDataToDocService saveDataToDocService) {
        super(saveDataToDocService);
        this.currencyService = privatbankCurrencyService;
    }
}

