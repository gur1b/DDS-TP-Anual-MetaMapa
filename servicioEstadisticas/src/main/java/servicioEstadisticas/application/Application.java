package servicioEstadisticas.application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;
import seeders.RepositoryServicioEstadisticasSeeder;
import servicioEstadisticas.DTO.HechoDTO;
import servicioEstadisticas.GeneradorTodasEstadisticas;
import servicioEstadisticas.model.entities.Hecho;
import servicioEstadisticas.model.entities.SolicitudSpam;
import servicioEstadisticas.model.repository.RepositoryServicioEstadisticas;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableScheduling
@RestController
@RequestMapping("/servicioEstadisticas")
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private static GeneradorTodasEstadisticas generadorTodasEstadisticas = GeneradorTodasEstadisticas.getInstance();

    public Application() {this.generadorTodasEstadisticas = GeneradorTodasEstadisticas.getInstance();
    }

    public static void main(String[] args) {
        log.info("[BOOT] Iniciando Servicio de Estadísticas...");
        SpringApplication.run(Application.class, args);
        RepositoryServicioEstadisticasSeeder repoSeeder = RepositoryServicioEstadisticasSeeder.getInstance();
        //repoSeeder.cargarHechos();
        try {
            // repoSeeder.cargarHechos();
            log.info("[BOOT] Generando estadísticas iniciales...");
            generadorTodasEstadisticas.actualizarEstadisticas();
            log.info("[BOOT] Estadísticas iniciales generadas correctamente");
        } catch (Exception e) {
            log.error("[BOOT] Error generando estadísticas iniciales", e);
        }
    }

    @GetMapping("/health")
    public String health() {
        return "servicio de estadisticas ACTIVA";
    }

    @GetMapping("/provincia-con-mas-hechos")
    public ResponseEntity<List<Map<String, Object>>> provinciaConMasHechos() {
        log.info("[GET] /provincia-con-mas-hechos - Obteniendo provincia con más hechos");
        List<Map<String, Object>> provincias = generadorTodasEstadisticas.getProvinciaConMasHechos();
        return ResponseEntity.ok(provincias);
    }

    @GetMapping("/categoria-mayor-cantidad")
    public ResponseEntity<List<Map<String, Object>>> CategoriaMayorCantidad() {
        log.info("[GET] /categoria-mayor-cantidad - Obteniendo categorías más reportadas");
        List<Map<String, Object>> categorias = generadorTodasEstadisticas.getCategoriaMasReportada();
        return ResponseEntity.ok(categorias);
    }


    @GetMapping("/cantidad-spam")
    public ResponseEntity<Map<String, Object>> cantidadSpam() {
        log.info("[GET] /cantidad-spam - Obteniendo cantidad de solicitudes de spam");
        Map<String, Object> estadisticas = generadorTodasEstadisticas.getCantSolicitudesEliminacion();
        return ResponseEntity.ok(estadisticas);
    }

    @GetMapping("/provincia-con-mas-hechos-por-categoria")
    public ResponseEntity<List<Map<String, Object>>> provinciaConMasHechosPorCategoria(
            @RequestParam("categoria") String categoria) {
        log.info("[GET] /provincia-con-mas-hechos-por-categoria - categoria={}", categoria);
        if (categoria == null || categoria.isBlank()) {
            log.warn("[GET] /provincia-con-mas-hechos-por-categoria - categoria vacía o null");
            return ResponseEntity.badRequest().build();
        }

        List<Map<String, Object>> provincias =
                generadorTodasEstadisticas.provinciaConMasHechosEnCategoria(categoria);
        return ResponseEntity.ok(provincias);
    }


    @GetMapping("/horario-categoria")
    public ResponseEntity<List<Map<String, Object>>> horarioPorCategoria(@RequestParam("categoria") String categoria) {
        log.info("[GET] /horario-categoria - categoria={}", categoria);
        if (categoria == null || categoria.isBlank()) {
            log.warn("[GET] /horario-categoria - categoria vacía o null");
            return ResponseEntity.badRequest().build();
        }


        List<Map<String, Object>> horarios = generadorTodasEstadisticas.horarioxCategoria(categoria);
        return ResponseEntity.ok(horarios);
    }

    @GetMapping(value = "/export/csv/provincia-mas-hechos")
    public ResponseEntity<byte[]> exportProvinciaMasHechosCsv() {
        log.info("[EXPORT] /export/csv/provincia-mas-hechos - Generando CSV");
        generadorTodasEstadisticas.actualizarEstadisticas();
        List<Map<String, Object>> data = generadorTodasEstadisticas.getProvinciaConMasHechos();

        String csv = toCsv(data, List.of("provincia", "cantidad"));
        return asAttachment(csv, "provincia_mas_hechos.csv");
    }

    @GetMapping(value = "/export/csv/categoria-mas-reportada")
    public ResponseEntity<byte[]> exportCategoriaMasReportadaCsv() {
        log.info("[EXPORT] /export/csv/categoria-mas-reportada - Generando CSV");
        generadorTodasEstadisticas.actualizarEstadisticas();
        List<Map<String, Object>> data = generadorTodasEstadisticas.getCategoriaMasReportada();

        String csv = toCsv(data, List.of("categoria", "cantidad"));
        return asAttachment(csv, "categoria_mas_reportada.csv");
    }

    @GetMapping(value = "/export/csv/solicitudes-spam")
    public ResponseEntity<byte[]> exportSolicitudesSpamCsv() {
        log.info("[EXPORT] /export/csv/solicitudes-spam - Generando CSV");
        generadorTodasEstadisticas.actualizarEstadisticas();
        Map<String, Object> unico = generadorTodasEstadisticas.getCantSolicitudesEliminacion();

        List<Map<String, Object>> data = List.of(unico);
        String csv = toCsv(data, List.of("solicitudes spam", "total de solicitudes"));
        return asAttachment(csv, "solicitudes_spam.csv");
    }

    @GetMapping("/export/csv/horario-por-categoria/{categoria}")
    public ResponseEntity<byte[]> exportHorarioPorCategoriaCsv(@PathVariable("categoria") String categoria) {
        log.info("[EXPORT] /export/csv/horario-por-categoria/{} - Generando CSV", categoria);
        generadorTodasEstadisticas.actualizarEstadisticas();

        // Normaliza por las dudas
        categoria = java.net.URLDecoder.decode(categoria, StandardCharsets.UTF_8);
        categoria = categoria.replace("+", " ").replace("\"", "");

        List<Map<String, Object>> data = generadorTodasEstadisticas.horarioxCategoria(categoria);
        String csv = toCsv(data, List.of("hora", "cantidad"));

        return asAttachment(csv, "horario_por_categoria_" + categoria + ".csv");
    }

    @GetMapping("/export/csv/provincia-por-categoria/{categoria}")
    public ResponseEntity<byte[]> exportProvinciaPorCategoriaCsv(@PathVariable("categoria")  String categoria) {
        log.info("[EXPORT] /export/csv/provincia-por-categoria/{} - Generando CSV (antes de normalizar)", categoria);
        generadorTodasEstadisticas.actualizarEstadisticas();
        List<Map<String, Object>> data = generadorTodasEstadisticas.provinciaConMasHechosEnCategoria(categoria);

        // Normaliza por las dudas
        categoria = java.net.URLDecoder.decode(categoria, StandardCharsets.UTF_8);
        categoria = categoria.replace("+", " ").replace("\"", "");

        String csv = toCsv(data, List.of("provincia", "cantidad"));
        return asAttachment(csv, "provincia_mas_hechos_categoria_" + categoria + ".csv");
    }


    private String toCsv(List<Map<String, Object>> rows, List<String> headers) {
        StringBuilder sb = new StringBuilder();

        sb.append(String.join(",", headers)).append("\n");

        for (Map<String, Object> row : rows) {
            for (int i = 0; i < headers.size(); i++) {
                Object val = row.get(headers.get(i));
                String cell = (val == null) ? "" : String.valueOf(val);
                cell = "\"" + cell.replace("\"", "\"\"") + "\"";
                sb.append(cell);
                if (i < headers.size() - 1) sb.append(",");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private ResponseEntity<byte[]> asAttachment(String csv, String filename) {
        log.info("[CSV] Enviando archivo CSV como attachment filename={}", filename);
        byte[] bytes = csv.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(org.springframework.http.MediaType.valueOf("text/csv"))
                .body(bytes);
    }

}