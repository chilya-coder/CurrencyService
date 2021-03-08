package com.chimyrys.currencyservice.config;

import com.chimyrys.currencyservice.model.converter.JsonToMonoExchangeRateResponse;
import com.chimyrys.currencyservice.model.converter.JsonToPrivatExchangeRateResponse;
import com.chimyrys.currencyservice.model.converter.MonoExchangeRateToExchangeRate;
import com.chimyrys.currencyservice.model.converter.PrivatExchangeRateToExchangeRate;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(true).
                favorParameter(false).
                ignoreAcceptHeader(true).
                defaultContentType(MediaType.APPLICATION_XML).
                mediaType("json", MediaType.APPLICATION_JSON).
                mediaType("xml", MediaType.APPLICATION_XML);
    }
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new JsonToMonoExchangeRateResponse());
        registry.addConverter(new MonoExchangeRateToExchangeRate());
        registry.addConverter(new JsonToPrivatExchangeRateResponse());
        registry.addConverter(new PrivatExchangeRateToExchangeRate());
    }
}
