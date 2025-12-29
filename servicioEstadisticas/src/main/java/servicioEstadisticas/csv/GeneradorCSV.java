package servicioEstadisticas.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GeneradorCSV {

    private static final String DEFAULT_DIR = "servicioEstadisticas/estadisticasGeneradas/";

    private static final Logger log = LoggerFactory.getLogger(GeneradorCSV.class);

    public  void writeCsv(String fileName,
                                List<Map<String, String>> rows,
                                List<String> columns) throws IOException {

        String timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now());
        if (fileName == null || fileName.isBlank()) {
            fileName = "reporte_" + timestamp + ".csv";
        } else {
            if (!fileName.endsWith(".csv")) fileName += ".csv";
            int dotIndex = fileName.lastIndexOf(".csv");
            fileName = fileName.substring(0, dotIndex) + "_reporte_" + timestamp + ".csv";
        }

        if (columns == null || columns.isEmpty()) {
            LinkedHashSet<String> inferred = new LinkedHashSet<>();
            for (Map<String, String> r : rows) {
                if (r != null) inferred.addAll(r.keySet());
            }
            columns = new ArrayList<>(inferred);
        }

        Path outDir = Path.of(DEFAULT_DIR);
        Files.createDirectories(outDir);
        Path outPath = outDir.resolve(fileName);

        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setDelimiter(';')
                .setHeader(columns.toArray(String[]::new))
                .setQuoteMode(QuoteMode.MINIMAL)
                .build();

        try (FileWriter writer = new FileWriter(outPath.toFile());
             CSVPrinter printer = new CSVPrinter(writer, format)) {

            for (Map<String, String> row : rows) {
                List<String> orderedValues = new ArrayList<>();
                for (String col : columns) {
                    orderedValues.add(row.getOrDefault(col, ""));
                }
                printer.printRecord(orderedValues);
            }
        }

        log.info("[CSV] Archivo CSV generado correctamente en: {}", outPath.toAbsolutePath());
    }

    public  List<Map<String, String>> toStringRows(List<Map<String, Object>> rows) {
        if (rows == null) return List.of();
        List<Map<String, String>> out = new ArrayList<>(rows.size());
        for (Map<String, Object> row : rows) {
            out.add(toStringRow(row));
        }
        return out;
    }

    public  Map<String, String> toStringRow(Map<String, Object> row) {
        if (row == null) return Map.of();
        Map<String, String> r = new LinkedHashMap<>();
        for (Map.Entry<String, Object> e : row.entrySet()) {
            r.put(e.getKey(), String.valueOf(e.getValue()));
        }
        return r;
    }

    private static volatile GeneradorCSV instance;

    public static GeneradorCSV getInstance() {
        if (instance == null) {
            synchronized (GeneradorCSV.class) {
                if (instance == null) {
                    instance = new GeneradorCSV();
                }
            }
        }
        return instance;
    }


}
