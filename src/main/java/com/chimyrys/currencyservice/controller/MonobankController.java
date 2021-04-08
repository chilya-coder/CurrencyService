package com.chimyrys.currencyservice.controller;

import com.chimyrys.currencyservice.service.api.CurrencyService;
import com.chimyrys.currencyservice.service.api.SaveDataToDocService;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monobank")
public class MonobankController extends AbstractController {

    public MonobankController(CurrencyService monobankCurrencyService, SaveDataToDocService saveDataToDocService, Environment env) {
        super(saveDataToDocService, env);
        this.currencyService = monobankCurrencyService;
    }
}
