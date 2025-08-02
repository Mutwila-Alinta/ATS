package ats;

import com.ats.pricing.tools.DayCountConvention;

import java.time.LocalDate;

public class FixedLeg extends OISLeg {
    private final double fixedRate;

    public FixedLeg(LocalDate startDate, LocalDate endDate, double notional,
                    double fixedRate, DayCountConvention dayCount, DiscountCurve discountCurve) {
        super(startDate, endDate, notional, dayCount, discountCurve);
        this.fixedRate = fixedRate;
    }

    @Override
    public double presentValue() {
        double yf = dayCount.yearFraction(startDate, endDate);
        double df = discountCurve.getDiscountFactor(endDate);
        return notional * fixedRate * yf * df;
    }
}

