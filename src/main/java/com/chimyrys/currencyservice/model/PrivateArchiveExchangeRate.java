package com.chimyrys.currencyservice.model;

/**
 * Class-model of objects we get from json response
 * from {privat.url_monthly}
 */
public class PrivateArchiveExchangeRate {
    private long date;
    private float sellPrice;
    private float buyPrice;

    public PrivateArchiveExchangeRate(long date, float sellPrice, float buyPrice) {
        this.date = date;
        this.sellPrice = sellPrice;
        this.buyPrice = buyPrice;
    }

    public PrivateArchiveExchangeRate() {
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public float getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(float sellPrice) {
        this.sellPrice = sellPrice;
    }

    public float getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(float buyPrice) {
        this.buyPrice = buyPrice;
    }

}
