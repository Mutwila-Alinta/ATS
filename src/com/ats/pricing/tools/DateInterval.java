package com.ats.pricing.tools;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

public final class DateInterval {


    private final LocalDate startDate;
    private final boolean isStartInclusive;
    private final LocalDate endDate;
    private final boolean isEndInclusive;

    public DateInterval(LocalDate startDate, boolean isStartInclusive,
                        LocalDate endDate, boolean isEndInclusive) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate must be on or before endDate");
        }
        if (startDate.equals(endDate) && isStartInclusive != isEndInclusive) {
            throw new IllegalArgumentException("isStartInclusive must equal isEndInclusive when startDate == endDate");
        }

        this.startDate = startDate;
        this.isStartInclusive = isStartInclusive;
        this.endDate = endDate;
        this.isEndInclusive = isEndInclusive;
    }

    public DateInterval(LocalDate startDate, boolean isStartInclusive,
                        LocalDate endDate, boolean isEndInclusive,
                        String uuid) {
        this(startDate, isStartInclusive, endDate, isEndInclusive);
    }

    public DateInterval(LocalDate startDate, LocalDate endDate) {
        this(startDate, true, endDate, false);
    }

    public static DateInterval getUpToInclusive(LocalDate date) {
        return new DateInterval(LocalDate.MIN, true, date, true);
    }

    public static DateInterval getFromInclusive(LocalDate date) {
        return new DateInterval(date, true, LocalDate.MAX, true);
    }

    public static DateInterval getUpToExclusive(LocalDate date) {
        return new DateInterval(LocalDate.MIN, true, date, false);
    }

    public static DateInterval getFromExclusive(LocalDate date) {
        return new DateInterval(date, false, LocalDate.MAX, true);
    }

    public boolean contains(LocalDate date) {
        Objects.requireNonNull(date);
        boolean afterStart = isStartInclusive ? !date.isBefore(startDate) : date.isAfter(startDate);
        boolean beforeEnd = isEndInclusive ? !date.isAfter(endDate) : date.isBefore(endDate);
        return afterStart && beforeEnd;
    }

    public boolean after(LocalDate date) {
        return isStartInclusive ? startDate.isAfter(date) : !startDate.isBefore(date);
    }

    public boolean before(LocalDate date) {
        return isEndInclusive ? endDate.isBefore(date) : !endDate.isAfter(date);
    }

    public long getLengthInDays() {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    public DateInterval union(DateInterval other) {
        Objects.requireNonNull(other);

        LocalDate newStart = startDate.isAfter(other.startDate) ? startDate : other.startDate;
        boolean newStartInclusive = startDate.equals(other.startDate)
                ? isStartInclusive && other.isStartInclusive
                : startDate.isAfter(other.startDate) ? isStartInclusive : other.isStartInclusive;

        LocalDate newEnd = endDate.isBefore(other.endDate) ? endDate : other.endDate;
        boolean newEndInclusive = endDate.equals(other.endDate)
                ? isEndInclusive && other.isEndInclusive
                : endDate.isBefore(other.endDate) ? isEndInclusive : other.isEndInclusive;

        if (newStart.isAfter(newEnd)) {
            return new DateInterval(newStart, false, newStart, false); // empty interval
        }

        if (newStart.equals(newEnd)) {
            newStartInclusive = newStartInclusive && newEndInclusive;
            newEndInclusive = newStartInclusive;
        }

        return new DateInterval(newStart, newStartInclusive, newEnd, newEndInclusive);
    }

    public String getDisplayName() {
        String open = isStartInclusive ? "[" : "(";
        String close = isEndInclusive ? "]" : ")";
        return open + startDate + "; " + endDate + close;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public boolean isStartInclusive() {
        return isStartInclusive;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isEndInclusive() {
        return isEndInclusive;
    }

    public DateInterval copy() {
        return new DateInterval(startDate, isStartInclusive, endDate, isEndInclusive);
    }

    @Override
    public String toString() {
        return (isStartInclusive ? "[" : "(") + startDate + ", " + endDate + (isEndInclusive ? "]" : ")");
    }

    public static void sortAscending(DateInterval[] intervals) {
        Arrays.sort(intervals, Comparator.comparing(DateInterval::getStartDate)
                .thenComparing((d) -> !d.isStartInclusive()));
    }

    public static void sortDescending(DateInterval[] intervals) {
        Arrays.sort(intervals, Comparator.comparing(DateInterval::getStartDate)
                .thenComparing((d) -> d.isStartInclusive()).reversed());
    }

    public static boolean intersects(DateInterval a, DateInterval b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);

        if (a == b) return true;
        if (a.endDate.equals(b.startDate)) return a.isEndInclusive && b.isStartInclusive;
        if (b.endDate.equals(a.startDate)) return b.isEndInclusive && a.isStartInclusive;

        return a.contains(b.startDate) || a.contains(b.endDate)
                || b.contains(a.startDate) || b.contains(a.endDate);
    }

    public static boolean isDisjoint(DateInterval[] intervals) {
        for (int i = 0; i < intervals.length - 1; i++) {
            for (int j = i + 1; j < intervals.length; j++) {
                if (intersects(intervals[i], intervals[j])) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void validateDisjoint(DateInterval[] intervals) {
        if (Arrays.stream(intervals).anyMatch(Objects::isNull)) {
            throw new NullPointerException("Interval array contains null elements");
        }
        if (!isDisjoint(intervals)) {
            throw new IllegalArgumentException("Interval array contains intersecting elements");
        }
    }
}
