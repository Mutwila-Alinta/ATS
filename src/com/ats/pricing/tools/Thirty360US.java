package com.ats.pricing.tools;

import java.time.LocalDate;

public class Thirty360US implements DayCountConvention {

    @Override
    public double calculateDayCountFraction(LocalDate startDate, LocalDate endDate) {
        int d1 = Math.min(startDate.getDayOfMonth(), 30);
        int d2 = endDate.getDayOfMonth() == 31 && d1 == 30 ? 30 : endDate.getDayOfMonth();
        int m1 = startDate.getMonthValue();
        int m2 = endDate.getMonthValue();
        int y1 = startDate.getYear();
        int y2 = endDate.getYear();

        int dayCount = 360 * (y2 - y1) + 30 * (m2 - m1) + (d2 - d1);
        return dayCount / 360.0;
    }
}
