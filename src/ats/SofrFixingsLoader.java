package ats;

// Realistic OIS Valuation Extension
// Adds: CSV import for SOFR, market discount/projection curves, business day calendar

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

class SofrFixingsLoader {
    public static Map<LocalDate, Double> loadFixings(String csvPath) throws IOException {
        Map<LocalDate, Double> fixings = new TreeMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                LocalDate date = LocalDate.parse(parts[0]);
                double rate = Double.parseDouble(parts[1]);
                fixings.put(date, rate);
            }
        }
        return fixings;
    }
}