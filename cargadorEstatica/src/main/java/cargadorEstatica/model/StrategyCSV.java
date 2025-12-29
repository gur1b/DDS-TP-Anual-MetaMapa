package cargadorEstatica.model;

import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class StrategyCSV implements StrategyTipoConexion {

    private static final Logger log = LoggerFactory.getLogger(StrategyCSV.class);
    public StrategyCSV() {}

    private static final String BASE_CSV_DIR = Paths.get("cargadorEstatica", "csv").toString();; // carpeta base

    @Override
    public List<HechoAIntegrarDTO> extraerHechosRecientes(String fuenteBase, String codigoFuente) {
        List<HechoAIntegrarDTO> hechos = new ArrayList<>();

        try {
            Path csvPath = Paths.get(BASE_CSV_DIR, fuenteBase);
            log.info("Procesando CSV fuente={} path={}", fuenteBase, csvPath.toAbsolutePath());
            if (!Files.exists(csvPath)) {
                log.warn("No se encontró el archivo CSV path={}", csvPath.toAbsolutePath());
                return List.of();
            }

            try (CSVReader reader = new CSVReader(Files.newBufferedReader(csvPath, StandardCharsets.UTF_8))) {
                String[] header = reader.readNext();
                if (header == null) {
                    log.warn("CSV vacío fuente={} path={}", fuenteBase, csvPath.toAbsolutePath());
                    return List.of();
                }

                String[] fila;
                while ((fila = reader.readNext()) != null) {

                    // Mínimo requerido
                    if (fila.length < 6) {
                        continue;
                    }

                    String titulo      = normalizar(fila[0]);
                    String descripcion = normalizar(fila[1]);
                    String categoria   = normalizar(fila[2]);
                    String latitud     = normalizar(fila[3]);
                    String longitud    = normalizar(fila[4]);
                    String fecha       = normalizar(fila[5]);

                    // Si falta algo esencial → skip
                    if (titulo == null || descripcion == null || categoria == null ||
                            latitud == null || longitud == null || fecha == null) {
                        continue;
                    }

                    // Opcionales (si no existen → null)
                    String horaSuceso     = fila.length > 6 ? normalizar(fila[6]) : null;
                    String contribuyente = fila.length > 7 ? normalizar(fila[7]) : null;
                    List<String> etiquetas =
                            fila.length > 8 ? parseEtiquetas(fila[8]) : null;

                    HechoAIntegrarDTO dto = new HechoAIntegrarDTO(
                            titulo, descripcion, categoria, latitud, longitud, fecha
                    );

                    dto.setHoraSuceso(horaSuceso);
                    dto.setContribuyente(contribuyente);
                    dto.setEtiquetas(etiquetas);

                    hechos.add(dto);
                }

            }
            }catch (IOException e) {
            log.error("Error al leer CSV fuente={}", fuenteBase, e);
        }catch (Exception e) {
            log.error("Error inesperado procesando CSV fuente={}", fuenteBase, e);
        }
        log.info("CSV procesado fuente={} hechosGenerados={}", fuenteBase, hechos.size());
        return hechos.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public String devolverTipoDeConexion() {
        return "CSV";
    }

    // ----------------- helpers -----------------

    private String normalizar(String s) {
        if (s == null) return null;
        String t = s.trim();
        if (t.isEmpty() || "null".equalsIgnoreCase(t)) return null;
        return t;
    }

    private List<String> parseEtiquetas(String raw) {
        raw = normalizar(raw);
        if (raw == null) return null;

        String sep = raw.contains(";") ? ";" : ",";
        List<String> tags = Arrays.stream(raw.split(sep))
                .map(String::trim)
                .filter(x -> !x.isEmpty())
                .toList();

        return tags.isEmpty() ? null : tags;
    }

}

