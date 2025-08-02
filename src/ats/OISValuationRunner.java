package ats;

import com.ats.pricing.tools.Actual360;
import com.ats.pricing.tools.DayCountConvention;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;

class OISValuationRunner {
    public static void main(String[] args) throws IOException {
        LocalDate valuationDate = LocalDate.of(2025, 7, 29);
        LocalDate startDate = LocalDate.of(2025, 4, 1);
        LocalDate endDate = LocalDate.of(2025, 7, 1);
        double notional = 1_000_000;
        double fixedRate = 0.035;

        Set<LocalDate> holidays = HolidayCalendarLoader.loadFromCsv("./holidays.csv");
        // Or use: HolidayCalendarLoader.loadFromJson("./holidays.json")
        BusinessDayCalendar calendar = new BusinessDayCalendar(holidays);
        DayCountConvention dayCount = new Actual360();

        Map<LocalDate, Double> sofrFixings = SofrFixingsLoader.loadFixings("./sofr_fixings.csv");
        NavigableMap<LocalDate, Double> discountCurveData = DiscountCurveLoader.loadFromCsv("./discount_curve.csv");
        // For JSON: DiscountCurveLoader.loadFromJson("./discount_curve.json");

        DiscountCurve discountCurve = new MarketDiscountCurve(valuationDate, discountCurveData);

        FixedLeg fixedLeg = new FixedLeg(startDate, endDate, notional, fixedRate, dayCount, discountCurve);
        HistoricalFloatingLeg floatingLeg = new HistoricalFloatingLeg(startDate, endDate, notional, dayCount, discountCurve, sofrFixings, calendar);

        double pvFixed = fixedLeg.presentValue();
        double pvFloat = floatingLeg.presentValue();
        double npv = pvFloat - pvFixed;

        System.out.printf("Fixed Leg PV: %.2f\n", pvFixed);
        System.out.printf("Floating Leg PV: %.2f\n", pvFloat);
        System.out.printf("OIS NPV: %.2f\n", npv);
    }
}