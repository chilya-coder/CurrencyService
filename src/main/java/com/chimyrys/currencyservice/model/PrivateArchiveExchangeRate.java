package com.chimyrys.currencyservice.model;

/**
 * Class-model of objects we get from json response
 * from {privat.url_monthly}
 */
public class PrivateArchiveExchangeRate {
    private long date;
    private long sellPrice;
    private long buyPrice;

    public PrivateArchiveExchangeRate(long date, long sellPrice, long buyPrice) {
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

    public long getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(long sellPrice) {
        this.sellPrice = sellPrice;
    }

    public long getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(long buyPrice) {
        this.buyPrice = buyPrice;
    }

}
