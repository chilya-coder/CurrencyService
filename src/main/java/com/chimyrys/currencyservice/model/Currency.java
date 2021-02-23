package com.chimyrys.currencyservice.model;

public enum Currency {
    RUB("RUB", 1),
    USD("USD", 2),
    EUR("EUR", 3),
    UAH("UAH", 4);
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
}
