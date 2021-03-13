package com.chimyrys.currencyservice.model.converter;

import com.chimyrys.currencyservice.model.PrivatbankArchiveExchangeRateResponse;
import com.chimyrys.currencyservice.model.PrivateArchiveExchangeRate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * Class that converts json to list of PrivateArchiveExchangeRate objects
 */
public class JsonToPrivateArchiveExchangeRate implements Converter <String, PrivatbankArchiveExchangeRateResponse> {

    @Override
    public PrivatbankArchiveExchangeRateResponse convert(String s) {
        try {
            return new PrivatbankArchiveExchangeRateResponse(new ObjectMapper().readValue(s, new TypeReference<List<PrivateArchiveExchangeRate>>() {}));
        } catch (JsonProcessingException e) {
            System.out.println("Can't convert");
        }
        return null;
    }
}
