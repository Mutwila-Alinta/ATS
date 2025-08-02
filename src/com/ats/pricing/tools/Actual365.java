package com.ats.pricing.tools;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Actual365 implements DayCountConvention {

    @Override
    public double calculateDayCountFraction(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate) / 365.0;
    }

    public double getDayCount(LocalDate date1, LocalDate date2) {
        return ChronoUnit.DAYS.between(date1, date2);
    }

    public double getDayCount(long millis1, long millis2) {
        LocalDate date1 = toLocalDate(millis1);
        LocalDate date2 = toLocalDate(millis2);
        return getDayCount(date1, date2);
    }

    private LocalDate toLocalDate(long millis) {
        return java.time.Instant.ofEpochMilli(millis)
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
    }
}