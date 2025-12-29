package cargadorProxy.model;

import cargadorProxy.repository.RepositoryFuentes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CargadorProxy {
    private static final Logger log = LoggerFactory.getLogger(CargadorProxy.class);

    private static CargadorProxy instance;
    private RepositoryFuentes repositoryFuentes = RepositoryFuentes.getInstance() ;
    private volatile Duration umbralProcesamiento = Duration.ofMinutes(30);

    public void setUmbral(Duration d) { this.umbralProcesamiento = d; }

    public static CargadorProxy getInstance() {
        if (instance == null) {
            synchronized (CargadorProxy.class) {
                if (instance == null) {
                    instance = new CargadorProxy();
                }
            }
        }
        return instance;
    }

    public List<HechoAIntegrarDTO> extraerHechosAIntegrar(){


        List<Fuente> fuentes = fuentesAProcesar();
        log.info("Fuentes a procesar: {}", fuentes.size());
        if(fuentes.isEmpty()) return List.of();

        if (fuentes.isEmpty()) {
            log.info("No hay fuentes para procesar");
            return List.of();
        }

        List<HechoAIntegrarDTO> hechos = new ArrayList<>();
        for (Fuente f : fuentes) {
            try{
                log.info("Procesando fuente id={} nombre={}", f.getId(), f.getNombre());
                List<HechoAIntegrarDTO> lote = f.extraerHechos();

                int cantidad = (lote != null) ? lote.size() : 0;
                log.info("Fuente id={} produjo {} hechos", f.getId(), cantidad);

                if (lote != null) {
                    lote.forEach(h -> h.setLinkFuente(f.getLink()));
                    hechos.addAll(lote);
                }
                f.setUltimoProcesamiento(Instant.now());
                repositoryFuentes.update(f);
            } catch (Exception e) {
                log.error("Error procesando fuente id={} nombre={}",
                        f.getId(),
                        f.getNombre(),
                        e
                );}

        }
        hechos.forEach(h -> h.setTipoFuente("PROXY"));
        return hechos;
    }

    public List<Fuente> fuentesAProcesar() {
        Instant corte = Instant.now().minus(umbralProcesamiento);
        return repositoryFuentes.findAll().stream()
                .filter(f -> f.getUltimoProcesamiento() == null || f.getUltimoProcesamiento().isBefore(corte))
                .toList();
    }

}
