package ats;

import com.ats.pricing.tools.DayCountConvention;

import java.time.LocalDate;

public abstract class OISLeg {
    protected final LocalDate startDate;
    protected final LocalDate endDate;
    protected final double notional;
    protected final DayCountConvention dayCount;
    protected final DiscountCurve discountCurve;

    public OISLeg(LocalDate startDate, LocalDate endDate, double notional,
                  DayCountConvention dayCount, DiscountCurve discountCurve) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.notional = notional;
        this.dayCount = dayCount;
        this.discountCurve = discountCurve;
    }

    public abstract double presentValue();
}

