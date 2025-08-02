package com.ats.pricing.foundation;

import com.ats.tradingsystem.utils.exceptions.ParsingException;
import com.fasterxml.jackson.annotation.JsonValue;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public final class TimePeriod implements Comparable<TimePeriod>, Cloneable {

    private final int years;
    private final int months;
    private final int weeks;
    private final int days;
    private final int hours;
    private final int minutes;
    private final int seconds;
    private final int milliseconds;

    public TimePeriod() {
        this(0, 0, 0, 0, 0, 0, 0, 0, false);
    }

    public TimePeriod(int years, int months, int weeks, int days, int hours, int minutes, int seconds, int milliseconds, boolean roundUp) {
        if (roundUp) {
            int ms = milliseconds;
            seconds += ms / 1000;
            this.milliseconds = ms % 1000;

            int s = seconds;
            minutes += s / 60;
            this.seconds = s % 60;

            int m = minutes;
            hours += m / 60;
            this.minutes = m % 60;

            int h = hours;
            days += h / 24;
            this.hours = h % 24;

            int d = days;
            weeks += d / 7;
            this.days = d % 7;

            int mo = months;
            years += mo / 12;
            this.months = mo % 12;

            this.weeks = weeks;
            this.years = years;
        } else {
            this.years = years;
            this.months = months;
            this.weeks = weeks;
            this.days = days;
            this.hours = hours;
            this.minutes = minutes;
            this.seconds = seconds;
            this.milliseconds = milliseconds;
        }
    }

    public TimePeriod(int years, int months, int weeks, int days, int hours, int minutes, int seconds, int milliseconds) {
        this(years, months, weeks, days, hours, minutes, seconds, milliseconds, true);
    }

    public TimePeriod(int years, int months, int weeks, int days) {
        this(years, months, weeks, days, 0, 0, 0, 0);
    }

    public TimePeriod(int years, int months, int days) {
        this(years, months, 0, days);
    }

    public TimePeriod(int years, int months) {
        this(years, months, 0, 0);
    }

    public TimePeriod(int years) {
        this(years, 0, 0, 0);
    }

    public static TimePeriod fromCode(String code, boolean roundUp) throws ParsingException {
        code = code.trim().toUpperCase();
        if (code.equals("0")) return new TimePeriod();

        if (code.length() > 1) {
            char unit = code.charAt(code.length() - 1);
            int value = Integer.parseInt(code.substring(0, code.length() - 1));
            return switch (unit) {
                case 'Y' -> new TimePeriod(value, 0, 0, 0, 0, 0, 0, 0, roundUp);
                case 'M' -> new TimePeriod(0, value, 0, 0, 0, 0, 0, 0, roundUp);
                case 'W' -> new TimePeriod(0, 0, value, 0, 0, 0, 0, 0, roundUp);
                case 'D' -> new TimePeriod(0, 0, 0, value, 0, 0, 0, 0, roundUp);
                case 'H' -> new TimePeriod(0, 0, 0, 0, value, 0, 0, 0, roundUp);
                default -> throw new ParsingException("Invalid time period specified: " + code);
            };
        }
        throw new ParsingException("Invalid time period format: " + code);
    }

    public static TimePeriod fromDays(int totalDays) {
        LocalDate start = LocalDate.now();
        Period period = Period.between(start, start.plusDays(totalDays));

        int years = period.getYears();
        int months = period.getMonths();
        int daysLeft = period.getDays();

        int weeks = daysLeft / 7;
        int days = daysLeft % 7;

        if (weeks > 0 && days >= 3) {
            weeks++;
            days = 0;
        }

        return new TimePeriod(years, months, weeks, days, 0, 0, 0, 0, false);
    }

    public int getSpecifiedElementCount() {
        return (int) List.of(years, months, weeks, days, hours, minutes, seconds, milliseconds)
                .stream().filter(i -> i != 0).count();
    }

    public TimePeriod multiplyBy(int x) {
        return x == 0 ? new TimePeriod() : new TimePeriod(
                years * x, months * x, weeks * x, days * x,
                hours * x, minutes * x, seconds * x, milliseconds * x);
    }

    public TimePeriod add(TimePeriod other) {
        return new TimePeriod(
                years + other.years,
                months + other.months,
                weeks + other.weeks,
                days + other.days,
                hours + other.hours,
                minutes + other.minutes,
                seconds + other.seconds,
                milliseconds + other.milliseconds);
    }

    public TimePeriod subtract(TimePeriod other) {
        return new TimePeriod(
                years - other.years,
                months - other.months,
                weeks - other.weeks,
                days - other.days,
                hours - other.hours,
                minutes - other.minutes,
                seconds - other.seconds,
                milliseconds - other.milliseconds);
    }

    public boolean isZero() {
        return years == 0 && months == 0 && weeks == 0 && days == 0
                && hours == 0 && minutes == 0 && seconds == 0 && milliseconds == 0;
    }

    public boolean isPaymentFrequencyGreaterThanYear() {
        return getPeriodInMonths().months > 12;
    }

    public TimePeriod getPeriodInMonths() {
        int totalMonths = months + (years * 12) + (days == 1 ? 1 : 0);
        return new TimePeriod(0, totalMonths, 0, 0, 0, 0, 0, 0, false);
    }

    @Override
    public String toString() {
        return getCode();
    }

    @JsonValue
    public String getCode() {
        List<String> parts = new ArrayList<>();
        if (years != 0) parts.add(years + "Y");
        if (months != 0) parts.add(months + "M");
        if (weeks != 0) parts.add(weeks + "W");
        if (days != 0) parts.add(days + "D");
        if (hours != 0) parts.add(hours + "H");
        if (minutes != 0) parts.add(minutes + "m");
        if (seconds != 0) parts.add(seconds + "s");
        if (milliseconds != 0) parts.add(milliseconds + "ms");

        return parts.isEmpty() ? "0D" : String.join(",", parts);
    }

    // Adjust ZonedDateTime using business calendar logic
    public ZonedDateTime getAdjustedDateBefore(ZonedDateTime dateTime, BusinessDayCalendar calendar) {
       return null; // return calendar.add(dateTime,  true);
    }

    // Overloaded method to maintain legacy Date compatibility
    public Date getAdjustedDateBeforeSpecifiedDate(Date date, BusinessDayCalendar businessDayCalendar) {
        ZonedDateTime zonedDate = date.toInstant().atZone(ZoneId.systemDefault());
        ZonedDateTime adjusted = getAdjustedDateBefore(zonedDate, businessDayCalendar);
        return Date.from(adjusted.toInstant());
    }


    public Date getAdjustedEndOfTheMonthDateBeforeSpecifiedDate(Date date) {
        ZonedDateTime zonedDate = date.toInstant().atZone(ZoneId.systemDefault());
        ZonedDateTime adjusted = zonedDate
                .minusYears(years)
                .minusMonths(months)
                .minusWeeks(weeks)
                .minusDays(days)
                .minusHours(hours)
                .minusMinutes(minutes)
                .minusSeconds(seconds)
                .minusNanos(milliseconds * 1_000_000L);
        LocalDate endOfMonth = YearMonth.from(adjusted).atEndOfMonth();
        return Date.from(endOfMonth.atStartOfDay(adjusted.getZone()).toInstant());
    }

    public ZonedDateTime getAdjustedDate(ZonedDateTime dateTime) {
        return dateTime
                .plusYears(years)
                .plusMonths(months)
                .plusWeeks(weeks)
                .plusDays(days)
                .plusHours(hours)
                .plusMinutes(minutes)
                .plusSeconds(seconds)
                .plusNanos(milliseconds * 1_000_000L);
    }

    public Date getAdjustedDate(Date date) {
        ZonedDateTime zonedDate = date.toInstant().atZone(ZoneId.systemDefault());
        ZonedDateTime adjusted = getAdjustedDate(zonedDate);
        return Date.from(adjusted.toInstant());
    }

    public ZonedDateTime getAdjustedDate() {
        return getAdjustedDate(ZonedDateTime.now());
    }

    public List<ZonedDateTime> getAdjustedDates(List<ZonedDateTime> dateTimes) {
        return dateTimes.stream().map(this::getAdjustedDate).toList();
    }

    public boolean after(TimePeriod other) {
        return this.compareTo(other) > 0;
    }

    public boolean before(TimePeriod other) {
        return this.compareTo(other) < 0;
    }

    @Override
    public int compareTo(TimePeriod o) {
        ZonedDateTime now = ZonedDateTime.now();
        return this.getAdjustedDate(now).compareTo(o.getAdjustedDate(now));
    }

    public Object getMBeanValue() {
        return this.getCode();
    }

    @Override
    public Object clone() {
        return this; // Immutable
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TimePeriod that)) return false;
        return years == that.years && months == that.months && weeks == that.weeks &&
                days == that.days && hours == that.hours && minutes == that.minutes &&
                seconds == that.seconds && milliseconds == that.milliseconds;
    }

    @Override
    public int hashCode() {
        return Objects.hash(years, months, weeks, days, hours, minutes, seconds, milliseconds);
    }

    // Getters
    public int years() { return years; }
    public int months() { return months; }
    public int weeks() { return weeks; }
    public int days() { return days; }
    public int hours() { return hours; }
    public int minutes() { return minutes; }
    public int seconds() { return seconds; }
    public int milliseconds() { return milliseconds; }
}
