package com.chimyrys.currencyservice.model.converter;


import com.chimyrys.currencyservice.model.privatbank.PrivatBankExchangeRateResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.core.convert.converter.Converter;

public class JsonToPrivatExchangeRateResponse implements Converter<String, PrivatBankExchangeRateResponse> {
    private final static Logger logger = Logger.getLogger(JsonToPrivatExchangeRateResponse.class);
    @Override
    public PrivatBankExchangeRateResponse convert(String s) {
        try {
            return new ObjectMapper().readValue(s, PrivatBankExchangeRateResponse.class);
        } catch (JsonProcessingException e) {
            logger.error("Can't convert" + s + " to " + PrivatBankExchangeRateResponse.class);
        }
        return null;
    }
}
