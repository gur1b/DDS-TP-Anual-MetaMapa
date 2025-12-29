package core.api.handlers.hechos;

import core.api.DTO.HechoResumenDTO;
import core.api.handlers.colecciones.UtilsFormatos;
import core.models.entities.hecho.Contribuyente;
import core.models.entities.hecho.Estado;
import core.models.repository.ContribuyentesRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.entities.colecciones.criterios.Criterio;
import core.models.entities.colecciones.criterios.CriterioFechaCarga;
import core.models.entities.colecciones.criterios.CriterioFechaSuceso;
import core.models.entities.colecciones.criterios.FiltradorColecciones;
import core.models.entities.hecho.Hecho;
import core.models.repository.HechosRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class GetHechoHandler implements Handler {

    private static final Logger log = LoggerFactory.getLogger(GetHechoHandler.class);

    private final HechosRepository repoHechos = HechosRepository.getInstance();
    private final ContribuyentesRepository repoContribuyentes = ContribuyentesRepository.getInstance();


    @Override
    public void handle(@NotNull Context context) throws Exception {
        /*
        UtilsFormatos utilsFormatos = new UtilsFormatos();

        String categoria = context.queryParam("categoria");
        String fechaReporteDesde = context.queryParam("fecha_reporte_desde");
        String fechaReporteHasta = context.queryParam("fecha_reporte_hasta");
        String fechaAcontecimientoDesde = context.queryParam("fecha_acontecimiento_desde");
        String fechaAcontecimientoHasta = context.queryParam("fecha_acontecimiento_hasta");
        String latitud = context.queryParam("latitud");
        String longitud = context.queryParam("longitud");

        List<Criterio> criterios = new ArrayList<>();

        if (categoria != null && !categoria.isBlank()) {
            criterios.add(utilsFormatos.transformarCategoriaEnCriterio(categoria));
        }

        if ((fechaReporteDesde != null && !fechaReporteDesde.isBlank()) || (fechaReporteHasta != null && fechaReporteHasta.isBlank())) {
            criterios.add(new CriterioFechaCarga(utilsFormatos.stringALocalDate(fechaReporteDesde), utilsFormatos.stringALocalDate(fechaReporteHasta)));
        }
        else {System.out.println("No se han pasado las fechas de reporte");}

        if (fechaAcontecimientoDesde != null || fechaAcontecimientoHasta != null) {
            criterios.add(new CriterioFechaSuceso(utilsFormatos.stringALocalDate(fechaAcontecimientoDesde), utilsFormatos.stringALocalDate(fechaAcontecimientoHasta)));
        }

        if (latitud != null && longitud != null) {criterios.add(utilsFormatos.transformarUbicacionEnCriterio(latitud, longitud));}

        List<Hecho> hechosTotales = repoHechos.obtenerTodas();
        List<Hecho> hechosFiltrados = FiltradorColecciones.getInstance().filtrarHechos(hechosTotales, criterios);

        context.json(hechosFiltrados);
        */


        // 1) Traigo todos los hechos (con multimedia) y me quedo solo con los ACEPTADOS
        List<Hecho> hechosTotales = repoHechos.findAllConMultimedia();

        List<Hecho> hechosAprobados = hechosTotales.stream()
                .filter(hecho -> Estado.ACEPTADO.equals(hecho.getEstado()))
                .toList();

        // 2) Leo el parámetro "contribuyente" que ahora es el MAIL del contribuyente
        String mailContribuyente = context.queryParam("contribuyente");

        List<Hecho> hechosFiltrados = hechosAprobados;

        if (mailContribuyente != null && !mailContribuyente.isBlank()) {
            log.info("Listar hechos aprobados contribuyenteMail={}", mailContribuyente);
            // 3) Busco el contribuyente por mail en el repo
            Optional<Contribuyente> contribOpt = repoContribuyentes.findByMail(mailContribuyente);

            // 3.a) Si no existe → error
            if (contribOpt.isEmpty()) {
                log.warn("Contribuyente no existe mail={}", mailContribuyente);
                context.status(404).json(Map.of(
                        "error", "Contribuyente no existe",
                        "mail", mailContribuyente
                ));
                return; // corto acá, no sigo
            }

            // 3.b) Si existe → obtengo su id
            Integer idContribuyente = contribOpt.get().getId();

            // 4) Filtro los hechos aprobados por ese id de contribuyente
            hechosFiltrados = hechosAprobados.stream()
                    .filter(h -> h.getContribuyente() != null
                            && Objects.equals(h.getContribuyente().getId(), idContribuyente))
                    .toList();
        }

        // 5) Paso a DTO y devuelvo JSON
        List<HechoResumenDTO> hechosDevolver = pasarDTO(hechosFiltrados);

        log.info("Hechos listados (aprobadosTotal={})",
                hechosAprobados.size());
        context.json(hechosDevolver);
    }

    public List<HechoResumenDTO> pasarDTO(List<Hecho> hechos){
        return hechos.stream().map(HechoResumenDTO::from).toList();
    }

}