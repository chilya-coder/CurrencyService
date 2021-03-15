package com.chimyrys.currencyservice.model.converter;

import com.chimyrys.currencyservice.model.MonoBankExchangeRateResponse;
import com.chimyrys.currencyservice.model.MonobankExchangeRate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * Class that uses spring Converter to conver JSON to MonobankExchangeRateResponse
 */
public class JsonToMonoExchangeRateResponse implements Converter<String, MonoBankExchangeRateResponse> {
    private final static Logger logger = Logger.getLogger(JsonToMonoExchangeRateResponse.class);
    @Override
    public MonoBankExchangeRateResponse convert(String s) {
        try {
            return new MonoBankExchangeRateResponse(new ObjectMapper().readValue(s, new TypeReference<List<MonobankExchangeRate>>() {}));
        } catch (JsonProcessingException e) {
            logger.error("Can't convert" + s + " to " + JsonToMonoExchangeRateResponse.class);
            System.out.println("Can't convert");
        }
        return null;
    }
}
