package com.chimyrys.currencyservice.model;

/**
 * Class that consists of json fields of monobank
 * currencyCodeA - ASCI code due to ISO 4217, rate we covert from
 * currencyCodeB - ASCI code due to ISO 4217, rate we covert to
 * date - date UNIX timestamp in seconds
 * rateBuy
 * rateSell
 * rateCross
 */
public class MonobankExchangeRate {
    private int currencyCodeA;
    private int currencyCodeB;
    private long date;
    private float rateBuy;
    private float rateSell;
    private float rateCross;

    public int getCurrencyCodeA() {
        return currencyCodeA;
    }

    public void setCurrencyCodeA(int currencyCodeA) {
        this.currencyCodeA = currencyCodeA;
    }

    public int getCurrencyCodeB() {
        return currencyCodeB;
    }

    public void setCurrencyCodeB(int currencyCodeB) {
        this.currencyCodeB = currencyCodeB;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public float getRateBuy() {
        return rateBuy;
    }

    public void setRateBuy(float rateBuy) {
        this.rateBuy = rateBuy;
    }

    public float getRateSell() {
        return rateSell;
    }

    public void setRateSell(float rateSell) {
        this.rateSell = rateSell;
    }

    public float getRateCross() {
        return rateCross;
    }

    public void setRateCross(float rateCross) {
        this.rateCross = rateCross;
    }

    @Override
    public String toString() {
        return "MonobankExchangeRate{" +
                "currencyCodeA=" + currencyCodeA +
                ", currencyCodeB=" + currencyCodeB +
                ", date=" + date +
                ", rateBuy=" + rateBuy +
                ", rateSell=" + rateSell +
                ", rateCross=" + rateCross +
                '}';
    }
}
