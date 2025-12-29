package cargadorProxy.application;
import cargadorProxy.observabilidad.MetricasCargadorProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import utils.DBUtils;
import javax.persistence.EntityManager;

import cargadorProxy.RepositoryFuentesSeeder;
import cargadorProxy.model.*;
import cargadorProxy.repository.RepositoryFuentes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.time.Duration;
import java.util.List;

@SpringBootApplication(scanBasePackages = "cargadorProxy")
@RestController
@RequestMapping("/cargadorProxy")
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private static RepositoryFuentesSeeder repoFuentesSeeder = RepositoryFuentesSeeder.getInstance();
    private final CargadorProxy cargadorProxy;
    private final RepositoryFuentes repoFuentes = RepositoryFuentes.getInstance();


    public Application() {
        this.cargadorProxy = CargadorProxy.getInstance();
    }

    public static void main(String[] args) {
        repoFuentesSeeder.cargarRepos();
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/health")
    public String health() {
        return "API Cargador Proxy ACTIVA";
    }

    @GetMapping("/obtenerHechos")
    public Mono<ResponseEntity<List<HechoAIntegrarDTO>>> obtenerHechos() {
        log.info("Solicitud para obtener hechos");
        return Mono.fromCallable(() -> {
                    List<HechoAIntegrarDTO> hechos = cargadorProxy.extraerHechosAIntegrar();
                    log.info("Hechos obtenidos correctamente. cantidad={}", hechos.size());
                    return hechos;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .timeout(Duration.ofSeconds(10))
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Error al obtener hechos desde CargadorProxy", e);
                    return Mono.just(ResponseEntity.ok(List.of()));
                });
    }


    @PostMapping("/agregarFuente")
    public ResponseEntity<?> agregarFuente(@RequestBody FuenteDTO fuenteDTO) {

        log.info("Solicitud para agregar fuente nombre={} tipo={}",
                fuenteDTO.getNombre(),
                fuenteDTO.getStrategyTipoConexion());

        StrategyTipoConexion strategyFuente = obtenerStrategyFuente(fuenteDTO.getStrategyTipoConexion());
        if (strategyFuente == null){log.warn("Tipo de fuente no reconocido: {}", fuenteDTO.getStrategyTipoConexion());
            return ResponseEntity.status(400).body("Tipo de fuente no reconocido");}

        Fuente fuente = new Fuente(fuenteDTO.getId(), fuenteDTO.getNombre(), fuenteDTO.getLink(), strategyFuente, fuenteDTO.getTipoFuente());
        repoFuentes.save(fuente);

        log.info("Fuente agregada correctamente id={} nombre={}",
                fuenteDTO.getId(),
                fuenteDTO.getNombre());

        return ResponseEntity.status(201).build();
    }

    private StrategyTipoConexion obtenerStrategyFuente(String strategyTipoConexion) {
        return switch (strategyTipoConexion) {
            case "BIBLIOTECA" -> new StrategyBibliotecaCliente();
            case "API REST", "APIREST" -> new StrategyAPIREST();
            default -> null;
        };
    }

    @GetMapping("/obtenerFuentes")
    public ResponseEntity<List<Fuente>> obtenerFuentes(){
        log.info("Solicitud para listar fuentes");
        List<Fuente> fuentes = repoFuentes.findAll();
        if (fuentes.isEmpty()) {
            log.info("No se encontraron fuentes");
            return ResponseEntity.status(204).build();
        }
        log.info("Fuentes obtenidas. cantidad={}", fuentes.size());
        return ResponseEntity.ok(fuentes);
    }

    @PostMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarFuente(@PathVariable("id") Integer id){
        log.info("Solicitud para eliminar fuente id={}", id);
        Fuente fuente = repoFuentes.findById(id);
        if (fuente == null) {
            log.warn("No se encontró la fuente a eliminar id={}", id);
            return ResponseEntity.status(404).build();
        }

        repoFuentes.deleteById(id);

        log.info("Fuente eliminada correctamente id={}", id);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/metricas")
    public ResponseEntity<?> metrics() {
        return ResponseEntity.ok(MetricasCargadorProxy.snapshot());
    }
}