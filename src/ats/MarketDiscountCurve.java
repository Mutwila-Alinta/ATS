package ats;

import java.time.LocalDate;
import java.util.Map;
import java.util.NavigableMap;

class MarketDiscountCurve extends DiscountCurve {
    private final NavigableMap<LocalDate, Double> curve;

    public MarketDiscountCurve(LocalDate valuationDate, NavigableMap<LocalDate, Double> curve) {
        super(valuationDate, 0.0); // unused flatRate
        this.curve = curve;
    }

    @Override
    public double getDiscountFactor(LocalDate date) {
        Map.Entry<LocalDate, Double> floor = curve.floorEntry(date);
        Map.Entry<LocalDate, Double> ceiling = curve.ceilingEntry(date);

        if (floor == null || ceiling == null || floor.equals(ceiling)) {
            return floor != null ? floor.getValue() : 1.0;
        }

        // Linear interpolation
        long daysTotal = ceiling.getKey().toEpochDay() - floor.getKey().toEpochDay();
        long daysFromFloor = date.toEpochDay() - floor.getKey().toEpochDay();
        double df = floor.getValue() + (ceiling.getValue() - floor.getValue()) * daysFromFloor / daysTotal;
        return df;
    }
}
