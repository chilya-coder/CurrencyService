package com.chimyrys.currencyservice.controller;

import com.chimyrys.currencyservice.service.api.CurrencyService;
import com.chimyrys.currencyservice.service.api.SaveInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monobank")
public class MonobankController extends Controller {

    public MonobankController(CurrencyService monobankCurrencyService, SaveInfoService saveInfoService) {
        super(saveInfoService);
        this.currencyService = monobankCurrencyService;
    }
}
