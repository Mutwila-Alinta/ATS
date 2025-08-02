package ats;

import java.time.LocalDate;

public class OvernightRateCurve {
    private final double overnightRate;

    public OvernightRateCurve(double overnightRate) {
        this.overnightRate = overnightRate;
    }

    public double getRate(LocalDate date) {
        // Flat rate for simplicity; real curves would interpolate
        return overnightRate;
    }
}

