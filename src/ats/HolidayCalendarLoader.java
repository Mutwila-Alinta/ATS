package ats;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class HolidayCalendarLoader {
    public static Set<LocalDate> loadFromCsv(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return br.lines().skip(1).map(LocalDate::parse).collect(Collectors.toSet());
        }
    }

    public static Set<LocalDate> loadFromJson(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<String> dates = mapper.readValue(new File(path), new TypeReference<>() {});
        return dates.stream().map(LocalDate::parse).collect(Collectors.toSet());
    }
}