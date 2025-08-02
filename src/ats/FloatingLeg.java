package ats;

import java.time.LocalDate;
import com.ats.pricing.tools.DayCountConvention;

public class FloatingLeg extends OISLeg {
    private final OvernightRateCurve rateCurve;

    public FloatingLeg(LocalDate startDate, LocalDate endDate, double notional,
                       DayCountConvention dayCount, DiscountCurve discountCurve, OvernightRateCurve rateCurve) {
        super(startDate, endDate, notional, dayCount, discountCurve);
        this.rateCurve = rateCurve;
    }

    @Override
    public double presentValue() {
        double accrued = 0.0;
        LocalDate date = startDate;

        while (date.isBefore(endDate)) {
            LocalDate nextDate = date.plusDays(1);
            double rate = rateCurve.getRate(date);
            double yf = dayCount.calculateDayCountFraction(date, nextDate);
            accrued += rate * yf;
            date = nextDate;
        }

        double df = discountCurve.getDiscountFactor(endDate);
        return notional * accrued * df;
    }
}
