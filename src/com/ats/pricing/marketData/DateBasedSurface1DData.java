package com.ats.pricing.marketData;

import com.ats.pricing.foundation.BusinessCenter;
import com.ats.pricing.foundation.BusinessDayCalendar;
import com.ats.pricing.foundation.ReferenceTimePeriod;
import com.ats.pricing.tools.BusinessDayConvention;

import java.time.LocalDate;
import java.time.Period;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class DateBasedSurface1DData {

    private final Map<LocalDate, Double> data;

    private DateBasedSurface1DData(Map<LocalDate, Double> data) {
        this.data = data;
    }

    public Map<LocalDate, Double> getData() {
        return data;
    }

    public static DateBasedSurface1DData newInstance(
            BusinessCenter businessCenter,
            BusinessDayConvention dayConvention,
            LocalDate anchorDate,
            Object[] dateValues,
            Double[] values) {

        Objects.requireNonNull(businessCenter, "businessCenter must not be null");
        Objects.requireNonNull(dayConvention, "dayConvention must not be null");
        Objects.requireNonNull(anchorDate, "anchorDate must not be null");
        Objects.requireNonNull(dateValues, "dateValues must not be null");
        Objects.requireNonNull(values, "values must not be null");

        if (dateValues.length != values.length) {
            throw new IllegalArgumentException("Date Based Surface 1D data is invalid: length mismatch.");
        }

        Map<LocalDate, Double> data = new TreeMap<>();

        BusinessDayCalendar calendar = BusinessDayCalendar.getInstance(businessCenter.getCode());

        for (int i = 0; i < dateValues.length; i++) {
            LocalDate translatedDate = translateRelative(anchorDate, dateValues[i], businessCenter, dayConvention, calendar);
            data.put(translatedDate, values[i]);
        }

        return new DateBasedSurface1DData(data);
    }

    private static LocalDate translateRelative(
            LocalDate anchorDate,
            Object key,
            BusinessCenter businessCenter,
            BusinessDayConvention dayConvention,
            BusinessDayCalendar calendar) {

        Objects.requireNonNull(key, "Date key must not be null");

        LocalDate date;

        if (key instanceof LocalDate ld) {
            date = ld;
        } else if (key instanceof ReferenceTimePeriod rtp) {
            // Assuming getTimePeriod() returns java.time.Period
            Period period = rtp.getTimePeriod();
            date = anchorDate.plus(period);
        } else if (key instanceof Integer days) {
            date = anchorDate.plusDays(days);
        } else {
            throw new IllegalArgumentException(
                    "Invalid type. Cannot transform object to LocalDate: " + key.getClass());
        }

        return dayConvention.getAdjustedDate(date, calendar);
    }
}
