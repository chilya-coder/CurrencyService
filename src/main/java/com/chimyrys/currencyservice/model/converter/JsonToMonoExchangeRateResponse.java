package com.chimyrys.currencyservice.model.converter;

import com.chimyrys.currencyservice.model.MonoBankExchangeRateResponse;
import com.chimyrys.currencyservice.model.MonobankExchangeRate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * Class that uses spring Converter to conver JSON to MonobankExchangeRateResponse
 */
public class JsonToMonoExchangeRateResponse implements Converter<String, MonoBankExchangeRateResponse> {
    @Override
    public MonoBankExchangeRateResponse convert(String s) {
        try {
            return new MonoBankExchangeRateResponse(new ObjectMapper().readValue(s, new TypeReference<List<MonobankExchangeRate>>() {}));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
