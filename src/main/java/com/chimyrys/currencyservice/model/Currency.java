package com.chimyrys.currencyservice.model;

import org.springframework.beans.propertyeditors.CurrencyEditor;

/**
 * Class that describes model of currency.
 * Consists of values of RUB/USD/EUR/UAH
 * and its three-digits id due to ISO 4217
 */
public enum Currency {
    RUB("RUB", 643),
    USD("USD", 840),
    EUR("EUR", 978),
    UAH("UAH", 980),
    DEFAULT_INSTANCE("",-1);
    private final String value;
    private final int id;
    Currency(String value, int id) {
        this.value = value;
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public int getId() {
        return id;
    }

    public Currency getValueFromId(int id) {
        for (Currency c : values()) {
            if (c.id == id) {
                return c;
            }
        }
        return null;
    }

}
