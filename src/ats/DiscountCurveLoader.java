package ats;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

class DiscountCurveLoader {
    public static NavigableMap<LocalDate, Double> loadFromCsv(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return br.lines()
                    .skip(1)
                    .map(line -> line.split(","))
                    .collect(Collectors.toMap(
                            parts -> LocalDate.parse(parts[0]),
                            parts -> Double.parseDouble(parts[1]),
                            (a, b) -> b,
                            TreeMap::new
                    ));
        }
    }

    public static NavigableMap<LocalDate, Double> loadFromJson(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Double> rawMap = mapper.readValue(new File(path), new TypeReference<>() {});
        NavigableMap<LocalDate, Double> result = new TreeMap<>();
        for (Map.Entry<String, Double> entry : rawMap.entrySet()) {
            result.put(LocalDate.parse(entry.getKey()), entry.getValue());
        }
        return result;
    }
}
