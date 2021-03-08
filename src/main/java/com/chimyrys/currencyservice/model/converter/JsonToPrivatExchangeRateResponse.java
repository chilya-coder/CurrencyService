package com.chimyrys.currencyservice.model.converter;


import com.chimyrys.currencyservice.model.PrivatBankExchangeRateResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;

public class JsonToPrivatExchangeRateResponse implements Converter<String, PrivatBankExchangeRateResponse> {
    @Override
    public PrivatBankExchangeRateResponse convert(String s) {
        try {
            return new ObjectMapper().readValue(s, PrivatBankExchangeRateResponse.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("Can't convert");
        }
        return null;
    }
}
