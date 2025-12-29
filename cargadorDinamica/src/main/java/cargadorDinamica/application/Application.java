package cargadorDinamica.application;

import cargadorDinamica.model.CargadorDinamico;
import cargadorDinamica.model.HechoAIntegrarDTO;
import cargadorDinamica.observabilidad.MetricasCargadorDinamico;
import cargadorDinamica.repository.DinamicaRepository;
import cargadorDinamica.repository.RepositoryFuentesSeeder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utils.DBUtils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;


@SpringBootApplication(scanBasePackages = "cargadorDinamica")
@RestController
@RequestMapping("/cargadorDinamico")
public class Application {

    private static RepositoryFuentesSeeder repoFuentesSeeder = RepositoryFuentesSeeder.getInstance();
    private final CargadorDinamico cargadorDinamico;
    private final DinamicaRepository dinamicaRepository;

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    // Constructor para inyección de dependencias
    public Application() {
        this.cargadorDinamico = CargadorDinamico.getInstance();
        this.dinamicaRepository = DinamicaRepository.getInstance();
    }

    public static void main(String[] args) {
        repoFuentesSeeder.cargarRepos();
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/health")
    public String health() {
        return "API Cargador Dinámico ACTIVA";
    }

    @GetMapping("/obtenerHechos")
    public ResponseEntity<List<HechoAIntegrarDTO>> obtenerHechos() {
        long t0 = System.currentTimeMillis();
        String span = MDC.get("spanId");
        log.info("GET /obtenerHechos: inicio span={}", span);
        try {
        List<HechoAIntegrarDTO> hechos = cargadorDinamico.extraerHechosAIntegrar();

        long dt = System.currentTimeMillis() - t0;

        int n = (hechos == null ? 0 : hechos.size());
        log.info("GET /obtenerHechos: fin items={} durationMs={} span={}", n, dt, span);

        return ResponseEntity.ok(hechos);
    }catch (Exception e) {
            long dt = System.currentTimeMillis() - t0;
            log.error("GET /obtenerHechos: error durationMs={} span={}", dt, span, e);
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/agregarHecho")
    public ResponseEntity<String> agregarHecho(@RequestBody HechoAIntegrarDTO hecho) {
        long t0 = System.currentTimeMillis();
        String span = MDC.get("spanId");
        String hash = (hecho == null ? null : hecho.getHash());

        if (hecho == null) {
            log.warn("POST /agregarHecho: body null");
            return ResponseEntity.badRequest().body("Body vacío");
        }

        log.info("POST /agregarHecho: inicio hash={}", hash);
        try {
        HechoAIntegrarDTO hechoDTO = new HechoAIntegrarDTO(
                hecho.getHash(),
                hecho.getTitulo(),
                hecho.getDescripcion(),
                hecho.getCategoria(),
                hecho.getLatitud(),
                hecho.getLongitud(),
                hecho.getFechaSuceso(),
                hecho.getHoraSuceso(),
                hecho.getEtiquetas(),
                hecho.getContribuyente(),
                hecho.getMultimedia()
        );

        hechoDTO.setTipoFuente("DINAMICA");
        hechoDTO.setLinkFuente("Cargado por la web");
        LocalDate fechaHoy = LocalDate.now();
        hechoDTO.setFechaCarga(String.valueOf(fechaHoy));

        dinamicaRepository.save(hechoDTO);
        long dt = System.currentTimeMillis() - t0;
        log.info("POST /agregarHecho: creado hash={} durationMs={}", hash, dt);
        return ResponseEntity.status(201).body("Hecho agregado correctamente");
        } catch (Exception e) {
            long dt = System.currentTimeMillis() - t0;
            log.error("POST /agregarHecho: error hash={} durationMs={}", hash, dt, e);
            return ResponseEntity.status(500).body("Error guardando hecho");
        }}

    @GetMapping("/metricas")
    public ResponseEntity<?> metrics() {
        return ResponseEntity.ok(MetricasCargadorDinamico.snapshot());
    }

}
