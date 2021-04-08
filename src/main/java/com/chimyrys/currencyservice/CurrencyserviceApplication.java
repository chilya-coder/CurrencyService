package com.chimyrys.currencyservice;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:/application.properties")
@SpringBootApplication
public class CurrencyserviceApplication {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        SpringApplication.run(CurrencyserviceApplication.class, args);
    }

}
