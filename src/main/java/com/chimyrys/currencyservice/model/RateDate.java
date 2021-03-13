package com.chimyrys.currencyservice.model;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class that desribes date in following format:
 * "yyyy.mm.dd"
 */
public class RateDate {
    private int day;
    private int month;
    private int year;

    public RateDate(String date) {
        String[] values = date.split("\\.");
        year = Integer.parseInt(values[0]);
        month = Integer.parseInt(values[1]);
        day = Integer.parseInt(values[2]);
    }
    public RateDate(String date, String format) {
        String[] dates = date.split("\\.");
        String[] formats = format.split("\\.");
        Map<String, String> map = new HashMap<>();
        for(int i = 0; i < dates.length; i++) {
            map.put(formats[i], dates[i]);
        }
        year = Integer.parseInt(map.get("yyyy"));
        month = Integer.parseInt(map.get("mm"));
        day = Integer.parseInt(map.get("dd"));
    }

    public RateDate() {
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

    /**
     * Method that parses UNIX timestamp seconds to "yyyy.mm.dd format"
     * @param timestamp
     * @return RateDate
     */
    public static RateDate createDateFromSeconds(long timestamp) {
        return createDateFromMillis(timestamp * 1000);

    }
    /**
     * Method that parses UNIX timestamp millis to "yyyy.mm.dd format"
     * @param timestamp
     * @return RateDate
     */
    public static RateDate createDateFromMillis(long timestamp) {
        Calendar mydate = Calendar.getInstance();
        mydate.setTimeInMillis(timestamp);
        return new RateDate(mydate.get(Calendar.YEAR) +
                "." + (mydate.get(Calendar.MONTH) + 1) +
                "."  + (mydate.get(Calendar.DAY_OF_MONTH)));

    }

    @Override
    public String toString() {
        return getYear() + "." + getMonth() + "." + getDay();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RateDate rateDate = (RateDate) o;
        return day == rateDate.day && month == rateDate.month && year == rateDate.year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, month, year);
    }

    public boolean isBefore(RateDate rateDate) {
        if (rateDate.getYear() != getYear()) {
            return rateDate.getYear() > getYear();
        }
        if (rateDate.getMonth() != getMonth()) {
            return rateDate.getMonth() > getMonth();
        }
        if (rateDate.getDay() != getDay()) {
            return rateDate.getDay() > getDay();
        }
        return false;
    }
    public boolean isAfter(RateDate rateDate) {
        return  !isBefore(rateDate) && !equals(rateDate);
    }
}
