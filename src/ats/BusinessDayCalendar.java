package ats;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Set;

class BusinessDayCalendar {
    private final Set<DayOfWeek> weekends = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
    private final Set<LocalDate> holidays;

    public BusinessDayCalendar(Set<LocalDate> holidays) {
        this.holidays = holidays;
    }

    public boolean isBusinessDay(LocalDate date) {
        return !weekends.contains(date.getDayOfWeek()) && !holidays.contains(date);
    }

    public LocalDate nextBusinessDay(LocalDate date) {
        LocalDate next = date;
        while (!isBusinessDay(next)) {
            next = next.plusDays(1);
        }
        return next;
    }
}