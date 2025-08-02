package com.ats.pricing.tools;

import com.ats.pricing.foundation.BusinessDayCalendar;
import com.fasterxml.jackson.annotation.JsonValue;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * BusinessDayConvention interface for adjusting dates based on business calendars.
 */
public interface BusinessDayConvention extends Comparable<BusinessDayConvention> {

    /**
     * Returns a short human readable description of this convention.
     *
     * @return a short human readable description of this convention.
     */
    String getDescription();

    @JsonValue
    String getCode();

    /**
     * Returns the adjusted date if it would otherwise fall on a day that is not
     * a valid business day in the relevant calendar.
     *
     * @param date the date whose business day adjustment is required
     * @param businessDayCalendar the business day calendar to be used in
     *        adjusting the relevant date.
     * @return the adjusted date.
     */
    LocalDate getAdjustedDate(LocalDate date, BusinessDayCalendar businessDayCalendar);

    /**
     * Returns the adjusted date if it would otherwise fall on a day that is not
     * a valid business day in the relevant calendars.
     *
     * @param date the date whose business day adjustment is required
     * @param primaryCalendar the primary business day calendar to be used
     * @param secondaryCalendars optional additional calendars
     * @return the adjusted date.
     */
    LocalDate getAdjustedDate(LocalDate date,
                              BusinessDayCalendar primaryCalendar,
                              List<BusinessDayCalendar> secondaryCalendars);

    /**
     * Convenience default method to adapt varargs to list automatically.
     */
    default LocalDate getAdjustedDate(LocalDate date,
                                      BusinessDayCalendar primaryCalendar,
                                      BusinessDayCalendar... secondaryCalendars) {
        Objects.requireNonNull(primaryCalendar, "Primary calendar must not be null");
        return getAdjustedDate(date, primaryCalendar, List.of(secondaryCalendars));
    }
}
