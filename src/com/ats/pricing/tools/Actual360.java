package com.ats.pricing.tools;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Actual360 implements DayCountConvention {

    @Override
    public double calculateDayCountFraction(LocalDate startDate, LocalDate endDate) {
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        return daysBetween / 360.0;
    }
}