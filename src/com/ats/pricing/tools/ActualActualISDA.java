package com.ats.pricing.tools;

import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.ChronoUnit;

public class ActualActualISDA implements DayCountConvention {
    @Override
    public double calculateDayCountFraction(LocalDate startDate, LocalDate endDate) {
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        boolean isLeap = Year.of(startDate.getYear()).isLeap();
        double denominator = isLeap ? 366.0 : 365.0;
        return daysBetween / denominator;
    }
}
