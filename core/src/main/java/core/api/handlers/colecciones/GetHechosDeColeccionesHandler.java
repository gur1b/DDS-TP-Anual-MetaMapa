package core.api.handlers.colecciones;

import core.api.DTO.HechoResumenDTO;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.entities.colecciones.Coleccion;
import core.models.entities.colecciones.criterios.Criterio;
import core.models.entities.colecciones.criterios.CriterioFechaCarga;
import core.models.entities.colecciones.criterios.CriterioFechaSuceso;
import core.models.entities.colecciones.criterios.FiltradorColecciones;
import core.models.entities.hecho.Hecho;
import core.models.repository.ColeccionesRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GetHechosDeColeccionesHandler implements Handler {

    private static final Logger log = LoggerFactory.getLogger(GetHechosDeColeccionesHandler.class);

    private final ColeccionesRepository repoColecciones = ColeccionesRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {

        UtilsFormatos utilsFormatos = new UtilsFormatos();

        Integer idBuscado = context.pathParamAsClass("id", Integer.class).get();
        log.info("Listar hechos de colección (filtrados) idColeccion={}", idBuscado);

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

        if ((fechaReporteDesde != null && !fechaReporteDesde.isBlank()) || (fechaReporteHasta != null && !fechaReporteHasta.isBlank())) {
            criterios.add(new CriterioFechaCarga(
                    utilsFormatos.stringALocalDate(fechaReporteDesde),
                    utilsFormatos.stringALocalDate(fechaReporteHasta)
            ));
        }

        if ((fechaAcontecimientoDesde != null && !fechaAcontecimientoDesde.isBlank()) ||
                (fechaAcontecimientoHasta != null && !fechaAcontecimientoHasta.isBlank())) {
            criterios.add(new CriterioFechaSuceso(
                    utilsFormatos.stringALocalDate(fechaAcontecimientoDesde),
                    utilsFormatos.stringALocalDate(fechaAcontecimientoHasta)
            ));
        }

        if (latitud != null && longitud != null) {criterios.add(utilsFormatos.transformarUbicacionEnCriterio(latitud, longitud));}

        var opt = repoColecciones.findByIdFetchHechosYContribuyente(idBuscado); // <<-- NUEVO
        if (opt.isEmpty()) {
            log.warn("Colección no encontrada idColeccion={}", idBuscado);
            context.status(404).result("Colección no encontrada con ID: " + idBuscado);
            return;
        }

        Coleccion coleccion = opt.get();
        List<Hecho> hechosFiltrados = FiltradorColecciones.getInstance().filtrarColeccion(coleccion, criterios);

        // devuelve DTOs para no tocar relaciones LAZY de Hecho!!
        var respuesta = hechosFiltrados.stream()
                .map(HechoResumenDTO::from)
                .toList();
        log.info("Hechos devueltos ok idColeccion={} count={}", idBuscado, respuesta.size());
        context.status(200).json(respuesta);
        context.status(200).json(respuesta);


    }
    }
