package com.ats.pricing.foundation;


import org.apache.poi.ss.usermodel.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class BusinessDayCalendar implements Comparable<BusinessDayCalendar> {

    public static final LocalDate MANDATORY_START_DATE = LocalDate.of(2000, 1, 1);
    public static final LocalDate MANDATORY_END_DATE = LocalDate.of(2050, 12, 31);

    private static final Map<String, BusinessDayCalendar> INSTANCES_WITH_DST = new ConcurrentHashMap<>();
    private static final Map<String, BusinessDayCalendar> INSTANCES_WITHOUT_DST = new ConcurrentHashMap<>();

    private final String code;
    private final BusinessCenter businessCenter;
    private final boolean useDST;
    private final NavigableSet<LocalDate> holidays;
    private final LocalDate minSupportedDate;
    private final LocalDate maxSupportedDate;


    private static final Path EXCEL_PATH = Path.of("config/holidays.xlsx");
    private static final Path CSV_PATH = Path.of("config/holidays.csv");

    private BusinessDayCalendar(BusinessCenter center, boolean useDST) {
        this.code = center.getCode();
        this.businessCenter = center;
        this.useDST = useDST;
        this.holidays = loadHolidayInfo(center.getCode());

        LocalDate holidayStart = holidays.first();
        LocalDate holidayEnd = holidays.last();

        this.minSupportedDate = holidayStart.isAfter(MANDATORY_START_DATE) ? holidayStart : MANDATORY_START_DATE;
        this.maxSupportedDate = holidayEnd.isBefore(MANDATORY_END_DATE) ? holidayEnd : MANDATORY_END_DATE;
    }

    public static BusinessDayCalendar getInstance(String code) {
        return INSTANCES_WITHOUT_DST.computeIfAbsent(code, k -> {
            BusinessCenter center = BusinessCenter.getInstance(k);
            return new BusinessDayCalendar(center, false);
        });
    }

    public static BusinessDayCalendar getInstanceWithDST(String code) {
        return INSTANCES_WITH_DST.computeIfAbsent(code, k -> {
            BusinessCenter center = BusinessCenter.getInstance(k);
            return new BusinessDayCalendar(center, true);
        });
    }

    public boolean isBusinessDay(LocalDate date) {
        return !isWeekend(date) && !holidays.contains(date);
    }

    public boolean isNonWeekendHoliday(LocalDate date) {
        return !isBusinessDay(date) && !isWeekend(date);
    }

    public LocalDate getNext(LocalDate date, boolean strictlyNext) {
        LocalDate current = strictlyNext ? date.plusDays(1) : date;
        while (!isBusinessDay(current)) {
            current = current.plusDays(1);
        }
        return current;
    }

    public LocalDate getPrevious(LocalDate date, boolean strictlyPrevious) {
        LocalDate current = strictlyPrevious ? date.minusDays(1) : date;
        while (!isBusinessDay(current)) {
            current = current.minusDays(1);
        }
        return current;
    }

    public LocalDate getModifiedFollowing(LocalDate date) {
        LocalDate following = getNext(date, false);
        if (date.getMonth() == following.getMonth()) {
            return following;
        }
        return getPrevious(date, false);
    }

    public LocalDate getModifiedPreceding(LocalDate date) {
        LocalDate preceding = getPrevious(date, false);
        if (date.getMonth() == preceding.getMonth()) {
            return preceding;
        }
        return getNext(date, false);
    }

    public int getBusinessDaysBetween(LocalDate start, LocalDate end) {
        int count = 0;
        LocalDate current = start;
        while (!current.isAfter(end)) {
            if (isBusinessDay(current)) {
                count++;
            }
            current = current.plusDays(1);
        }
        return count;
    }

    public BusinessCenter getBusinessCenter() {
        return businessCenter;
    }


    public String getCode() {
        return code;
    }

    public NavigableSet<LocalDate> getHolidays() {
        return Collections.unmodifiableNavigableSet(holidays);
    }

    public LocalDate getMinSupportedDate() {
        return minSupportedDate;
    }

    public LocalDate getMaxSupportedDate() {
        return maxSupportedDate;
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }
    public LocalDate add(ZonedDateTime dateTime, Object obj, boolean ttrue){
        return null;
    }
    /*private NavigableSet<LocalDate> loadHolidayInfo(String code) {
        try {
            Registry registry = RegistryManager.getRegistry();
            HolidaySource source = (HolidaySource) registry.getService(HolidaySource.class);
            SortedSet<Date> javaDates = source.getHolidayInfo(code);
            NavigableSet<LocalDate> localDates = new TreeSet<>();
            for (Date d : javaDates) {
                LocalDate localDate = ZonedDateTime.ofInstant(d.toInstant(), zoneId).toLocalDate();
                localDates.add(localDate);
            }
            return localDates;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load holiday info for code: " + code, e);
        }
    }*/

    private NavigableSet<LocalDate> loadHolidayInfo(String code) {
        NavigableSet<LocalDate> holidays = new TreeSet<>();

        if (Files.exists(EXCEL_PATH)) {
            try (InputStream fis = Files.newInputStream(EXCEL_PATH);
                 Workbook workbook = WorkbookFactory.create(fis)) {

                Sheet sheet = workbook.getSheet(code);
                if (sheet != null) {
                    for (Row row : sheet) {
                        Cell cell = row.getCell(0); // Assume column A
                        if (cell != null && cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                            Date date = cell.getDateCellValue();
                            //todo fix holidays.add(date.toInstant().atZone(zoneId).toLocalDate());
                        }
                    }
                    return holidays;
                }
            } catch (Exception e) {
                System.err.println("Failed to read Excel file: " + e.getMessage());
            }
        }

        if (Files.exists(CSV_PATH)) {
            try (BufferedReader reader = Files.newBufferedReader(CSV_PATH)) {
                String header = reader.readLine();
                if (header == null || !header.toLowerCase().contains("code")) {
                    throw new IllegalArgumentException("CSV file must include 'code' and 'date' columns");
                }

                String[] headers = header.split(",");
                int codeIndex = Arrays.asList(headers).indexOf("code");
                int dateIndex = Arrays.asList(headers).indexOf("date");

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > Math.max(codeIndex, dateIndex) &&
                            parts[codeIndex].trim().equalsIgnoreCase(code)) {
                        LocalDate date = LocalDate.parse(parts[dateIndex].trim());
                        holidays.add(date);
                    }
                }
                return holidays;

            } catch (Exception e) {
                System.err.println("Failed to read CSV file: " + e.getMessage());
            }
        }

        System.err.println("Warning: No holiday data found for code: " + code);
        return holidays; // return empty but valid
    }

    @Override
    public int compareTo(BusinessDayCalendar o) {
        return this.code.compareTo(o.code);
    }

    @Override
    public String toString() {
        return "BusinessDayCalendar{" +                "code='" + code + "'''" +        ", useDST=" + useDST +
                ", zoneId=" + "zoneId +                '}'";
    }
}
