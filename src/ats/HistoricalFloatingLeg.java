package ats;

import com.ats.pricing.tools.DayCountConvention;

import java.time.LocalDate;
import java.util.Map;

class HistoricalFloatingLeg extends OISLeg {
    private final Map<LocalDate, Double> sofrFixings;
    private final BusinessDayCalendar calendar;

    public HistoricalFloatingLeg(LocalDate startDate, LocalDate endDate, double notional,
                                 DayCountConvention dayCount, DiscountCurve discountCurve,
                                 Map<LocalDate, Double> sofrFixings, BusinessDayCalendar calendar) {
        super(startDate, endDate, notional, dayCount, discountCurve);
        this.sofrFixings = sofrFixings;
        this.calendar = calendar;
    }

    @Override
    public double presentValue() {
        double accrued = 0.0;
        LocalDate date = startDate;

        while (date.isBefore(endDate)) {
            LocalDate nextDate = calendar.nextBusinessDay(date.plusDays(1));
            Double rate = sofrFixings.get(date);
            if (rate == null) {
                throw new IllegalArgumentException("Missing SOFR fixing for: " + date);
            }
            double yf = dayCount.yearFraction(date, nextDate);
            accrued += rate * yf;
            date = nextDate;
        }

        double df = discountCurve.getDiscountFactor(endDate);
        return notional * accrued * df;
    }
}