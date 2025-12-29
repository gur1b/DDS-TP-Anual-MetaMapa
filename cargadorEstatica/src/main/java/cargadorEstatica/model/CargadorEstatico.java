package cargadorEstatica.model;
import cargadorEstatica.repository.RepositoryFuentes;

import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class CargadorEstatico {

    private static CargadorEstatico instance;
    private RepositoryFuentes repositoryFuentes = RepositoryFuentes.getInstance() ;

    public static CargadorEstatico getInstance() {
        if (instance == null) {
            synchronized (CargadorEstatico.class) {
                if (instance == null) {
                    instance = new CargadorEstatico();
                }
            }
        }
        return instance;
    }

    public List<HechoAIntegrarDTO> extraerHechosAIntegrar() {
        List<Fuente> fuentes = fuentesAProcesar();

        if (fuentes.isEmpty()) return List.of();

        List<HechoAIntegrarDTO> hechos = new ArrayList<>();
        for (Fuente f : fuentes) {
            try {
                List<HechoAIntegrarDTO> lote = f.extraerHechos();
                if (lote != null) {
                    lote.forEach(h -> h.setLinkFuente(f.getLink()));
                    lote.forEach(h -> h.setIdFuente(f.getId()));
                    hechos.addAll(lote);
                }
                f.setUltimoProcesamiento(Instant.now());
                repositoryFuentes.update(f);
            } catch (Exception e) {
                System.err.println("Error procesando fuente: " + f.getId() + ":" + e.getMessage());}
            }
        hechos.forEach(h -> h.setTipoFuente("ESTATICA"));
        return hechos;
        }

    //Devuelve las fuentes que debo procesar: nunca procesadas
    public List<Fuente> fuentesAProcesar() {
        return repositoryFuentes.findAll().stream()
                .filter(f -> f.getUltimoProcesamiento() == null)
                .toList();
    }
}

