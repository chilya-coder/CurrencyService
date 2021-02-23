package com.chimyrys.currencyservice.model;

public class Date {
    private int day;
    private int month;
    private int year;

    /*
    * sep = "."
    * yyyy.mm.dd
    * */
    public Date(String date) {
        String[] values = date.split("\\.");
        year = Integer.parseInt(values[0]);
        month = Integer.parseInt(values[1]);
        day = Integer.parseInt(values[2]);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
