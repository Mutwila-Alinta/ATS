package com.ats.pricing.tools;

import com.ats.pricing.foundation.BusinessDayCalendar;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of the Modified Following business day convention.
 */
public final class ModifiedFollowing implements BusinessDayConvention, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static final ModifiedFollowing instance = new ModifiedFollowing();

    private ModifiedFollowing() {
    }

    @Override
    public LocalDate getAdjustedDate(LocalDate date, BusinessDayCalendar businessDayCalendar) {
        return businessDayCalendar.getModifiedFollowing(date);
    }

    @Override
    public LocalDate getAdjustedDate(LocalDate date,
                                     BusinessDayCalendar primaryCalendar,
                                     List<BusinessDayCalendar> secondaryCalendars) {
        Objects.requireNonNull(secondaryCalendars, "Secondary calendars must not be null");
        BusinessDayCalendar[] calendars = secondaryCalendars.toArray(BusinessDayCalendar[]::new);
        return primaryCalendar.getModifiedFollowing(date);
    }

    @Override
    public String getDescription() {
        return "modified_following";
    }

    @Override
    public String toString() {
        return getDescription();
    }

    @Override
    public int compareTo(BusinessDayConvention other) {
        return (other == this) ? 0 : this.getDescription().compareTo(other.getDescription());
    }

    @Serial
    private Object readResolve() {
        return instance;
    }

    @Override
    public String getCode() {
        return "MODIFIED FOLLOWING";
    }
}
