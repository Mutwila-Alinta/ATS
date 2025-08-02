package ats;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DiscountCurve {
    private final LocalDate valuationDate;
    private final double flatRate;

    public DiscountCurve(LocalDate valuationDate, double flatRate) {
        this.valuationDate = valuationDate;
        this.flatRate = flatRate;
    }

    public double getDiscountFactor(LocalDate date) {
        long days = ChronoUnit.DAYS.between(valuationDate, date);
        return Math.exp(-flatRate * days / 360.0);
    }
}

