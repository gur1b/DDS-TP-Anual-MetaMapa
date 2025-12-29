package cargadorEstatica.application;
import cargadorEstatica.observabilidad.MetricasCargadorEstatica;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import cargadorEstatica.model.*;
import cargadorEstatica.repository.RepositoryFuentes;
import cargadorEstatica.repository.RepositoryFuentesSeeder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.*;
import java.util.List;

@SpringBootApplication(scanBasePackages = "cargadorEstatica")
@RestController
@RequestMapping("/cargadorEstatico")
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private static final RepositoryFuentesSeeder repositoryFuentesSeeder = RepositoryFuentesSeeder.getInstance();

    private final CargadorEstatico cargadorEstatico;
    private final RepositoryFuentes repoFuentes = RepositoryFuentes.getInstance();

    public Application() {
        repositoryFuentesSeeder.cargarRepos();
        this.cargadorEstatico = CargadorEstatico.getInstance();
    }

    public static void main(String[] args) {
        repositoryFuentesSeeder.cargarRepos();
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/health")
    public String health() {
        return "API Cargador Estatico ACTIVA";
    }

    @GetMapping(value = "/obtenerHechos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HechoAIntegrarDTO>> obtenerHechos() {
        log.info("Solicitud para obtener hechos");
        try {
            List<HechoAIntegrarDTO> hechos = cargadorEstatico.extraerHechosAIntegrar();
            log.info("Hechos obtenidos correctamente. cantidad={}", hechos.size());
            return ResponseEntity.ok(hechos);
        } catch (Exception e) {
            log.error("Error al obtener hechos (estático)", e);
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/agregarFuente")
    public ResponseEntity<?> agregarFuente(@RequestBody FuenteDTO fuenteDTO) {
        log.info("Solicitud para agregar fuente estática nombre={} tipoFuente={}",
                fuenteDTO.getNombre(), fuenteDTO.getTipoFuente());

        StrategyTipoConexion strategyFuente = obtenerStrategyFuente(fuenteDTO.getTipoFuente());
        if (strategyFuente == null) {
            log.warn("Tipo de fuente no reconocido: {}", fuenteDTO.getTipoFuente());
            return ResponseEntity.status(400).body("Tipo de fuente no reconocido");
        }
        Fuente fuente = new Fuente(fuenteDTO.getId(), fuenteDTO.getNombre(), fuenteDTO.getLink(), strategyFuente, fuenteDTO.getTipoFuente());
        repoFuentes.save(fuente);

        log.info("Fuente estática agregada correctamente id={} nombre={}",
                fuenteDTO.getId(), fuenteDTO.getNombre());

        return ResponseEntity.status(201).build();
    }

    private StrategyTipoConexion obtenerStrategyFuente(String tipoFuente) {
        if (tipoFuente.equals("ESTATICA")) return new StrategyCSV();
        else return null;
    }

    @GetMapping("/obtenerFuentes")
    public ResponseEntity<List<Fuente>> obtenerFuentes(){
        log.info("Solicitud para listar fuentes");
        List<Fuente> fuentes = repoFuentes.findAll();
        if (fuentes.isEmpty()) {
            log.info("No se encontraron fuentes (estático)");
            return ResponseEntity.status(204).build();
        }
        log.info("Fuentes obtenidas (estático). cantidad={}", fuentes.size());
        return ResponseEntity.ok(fuentes);
    }

    @PostMapping("/fuentes/{id}/csv")
    public ResponseEntity<?> subirCsv(
            @PathVariable("id") Integer fuenteId,
            @RequestParam("archivoCsv") MultipartFile archivoCsv
    ) {
        log.info("Solicitud para subir CSV fuenteId={} archivo={}",
                fuenteId,
                archivoCsv != null ? archivoCsv.getOriginalFilename() : "null");
        try {
            var fuente = repoFuentes.findById(fuenteId);
            if (fuente == null) {
                log.warn("Fuente no encontrada al subir CSV. fuenteId={}", fuenteId);
                return ResponseEntity.status(404).body("Fuente no encontrada");
            }

            Path carpeta = Paths.get("cargadorEstatica", "csv");
            Files.createDirectories(carpeta);

            String nombreArchivo = archivoCsv.getOriginalFilename();
            if (nombreArchivo == null || nombreArchivo.isBlank()) {
                nombreArchivo = "fuente_" + fuenteId + ".csv";
                log.debug("Nombre de archivo vacío, se asigna por defecto: {}", nombreArchivo);
            }

            Path destino = carpeta.resolve(nombreArchivo);
            Files.copy(archivoCsv.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            // guardar solo el nombre relativo, que StrategyCSV usa
            fuente.setLink(nombreArchivo);
            repoFuentes.save(fuente);

            log.info("CSV guardado correctamente fuenteId={} path={}",
                    fuenteId,
                    destino.toAbsolutePath());

            return ResponseEntity.status(201).body("CSV guardado correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al guardar CSV");
        }
    }

    @PostMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarFuente(@PathVariable("id") Integer id){
        log.info("Solicitud para eliminar fuente estática id={}", id);
        try {
            Fuente fuente = repoFuentes.findById(id);
            if (fuente == null) {
                log.warn("No se encontró la fuente a eliminar id={}", id);
                return ResponseEntity.status(404).build();
            }

            try {
                Path carpeta = Paths.get("cargadorEstatica", "csv");
                Path destino = carpeta.resolve(fuente.getLink());
                boolean deleted = Files.deleteIfExists(destino);
                log.info("Archivo físico {} id={} path={}",
                        deleted ? "eliminado" : "no existía",
                        id,
                        destino.toAbsolutePath());
            } catch (Exception e) {
                log.error("No se pudo borrar el archivo físico de la fuente id={} link={}",
                        id, fuente.getLink(), e);
                return ResponseEntity.status(500).body("No se pudo borrar archivo físico");
            }
            repoFuentes.deleteById(id);
            log.info("Fuente estática eliminada correctamente id={}", id);
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            log.error("Error al eliminar fuente estática id={}", id, e);
        return ResponseEntity.status(500).body("Error al eliminar fuente estática");
        }
    }

    @GetMapping("/metricas")
    public ResponseEntity<?> metrics() {
        return ResponseEntity.ok(MetricasCargadorEstatica.snapshot());
    }
}

