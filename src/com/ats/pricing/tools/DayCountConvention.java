package com.ats.pricing.tools;

import java.time.LocalDate;

public interface DayCountConvention {
    /**
     * Calculates the day count fraction between two dates.
     * @param startDate The start date
     * @param endDate The end date
     * @return The day count fraction
     */
    double calculateDayCountFraction(LocalDate startDate, LocalDate endDate);
}
