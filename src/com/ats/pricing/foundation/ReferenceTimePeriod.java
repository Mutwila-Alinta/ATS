package com.ats.pricing.foundation;

import com.ats.tradingsystem.utils.exceptions.ParsingException;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public final class ReferenceTimePeriod implements Comparable<ReferenceTimePeriod> {

    private final String code;
    private final TimePeriod timePeriod;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public ReferenceTimePeriod() {
        this.code = "1D";
        this.timePeriod = new TimePeriod(0, 0, 1);
        this.startDate = LocalDate.now();
        this.endDate = LocalDate.now();
    }

    public ReferenceTimePeriod(String code) throws ParsingException {
        this(TimePeriod.getInstance(code, false));
    }

    public ReferenceTimePeriod(String code, LocalDate startDate, LocalDate endDate) throws ParsingException {
        this(TimePeriod.getInstance(code, false), startDate, endDate);
    }

    public ReferenceTimePeriod(TimePeriod timePeriod, LocalDate startDate, LocalDate endDate) {
        this.timePeriod = Objects.requireNonNull(timePeriod);
        this.code = timePeriod.toString();
        this.startDate = Objects.requireNonNull(startDate);
        this.endDate = Objects.requireNonNull(endDate);
    }

    public ReferenceTimePeriod(TimePeriod timePeriod) {
        this(timePeriod, LocalDate.now(), LocalDate.now());
    }

    public static ReferenceTimePeriod getInstance(String code) {
        try {
            return new ReferenceTimePeriod(code);
        } catch (ParsingException e) {
            throw new IllegalArgumentException("Failed to parse time period", e);
        }
    }

    public static ReferenceTimePeriod getInstance(LocalDate startDate, LocalDate endDate) {
        try {
            String tenor = getTenorFromDates(startDate, endDate);
            return new ReferenceTimePeriod(tenor, startDate, endDate);
        } catch (ParsingException e) {
            throw new IllegalArgumentException("Failed to parse time period", e);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static ReferenceTimePeriod getDefault() {
        return getInstance("3M");
    }

    private static String getTenorFromDates(LocalDate startDate, LocalDate endDate) {
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        final String day = "D";
        final String month = "M";
        final String year = "Y";

        if (daysBetween < 25) {
            return daysBetween + day;
        }

        int monthsBetween = getMonths(startDate, endDate);
        if (monthsBetween % 12 == 0) {
            int years = (int) Math.round(daysBetween / 365.0);
            return years + year;
        } else {
            return monthsBetween + month;
        }
    }

    public static int getMonths(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("The start date must be before end date");
        }

        LocalDate normalizedStart = startDate.withDayOfMonth(1);
        LocalDate normalizedEnd = endDate.plusMonths(1).withDayOfMonth(1).minusDays(1);
        return (int) ChronoUnit.MONTHS.between(normalizedStart, normalizedEnd);
    }

    public String getCode() {
        return code;
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    public TimePeriod getOnlyMonths() {
        return timePeriod.getPeriodInMonths();
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public int compareTo(ReferenceTimePeriod other) {
        return (other == this) ? 0 : this.timePeriod.compareTo(other.timePeriod);
    }

    @Override
    public boolean equals(Object obj) {
        if (ReflectiveComparator.basicEquals(this, obj)) {
            ReferenceTimePeriod other = (ReferenceTimePeriod) obj;
            return timePeriod.equals(other.timePeriod);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return timePeriod.hashCode();
    }

    @Override
    public String toString() {
        return code;
    }

    public Object getMBeanValue() {
        return this.getCode();
    }

    public static final class UserType extends ImmutableVarCharUserType<ReferenceTimePeriod> {
        @Override
        public Class<ReferenceTimePeriod> returnedClass() {
            return ReferenceTimePeriod.class;
        }

        @Override
        public ReferenceTimePeriod toInstance(String text) {
            try {
                return (text != null && !"null".equalsIgnoreCase(text)) ? ReferenceTimePeriod.getInstance(text) : null;
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to instantiate object from string: " + text, e);
            }
        }

        @Override
        public String toVarChar(ReferenceTimePeriod instance) {
            return instance != null ? instance.toString() : null;
        }
    }
}
