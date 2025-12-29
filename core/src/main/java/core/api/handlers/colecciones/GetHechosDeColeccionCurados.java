package core.api.handlers.colecciones;

import core.api.DTO.FiltroHechoDTO;
import core.api.DTO.HechoResumenDTO;
import core.api.utils.FiltroHechosMapper;
import core.models.entities.colecciones.criterios.Criterio;
import core.models.entities.colecciones.criterios.FiltradorColecciones;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.entities.colecciones.Coleccion;
import core.models.entities.colecciones.ModoDeNavegacion;
import core.models.entities.hecho.Hecho;
import core.models.repository.ColeccionesRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;

public class GetHechosDeColeccionCurados implements Handler {

    private static final Logger log = LoggerFactory.getLogger(GetHechosDeColeccionCurados.class);

    private final ColeccionesRepository repoColecciones = ColeccionesRepository.getInstance();
    // podés inyectarlo también si ya lo tenés en algún lado
    private final GetHechosDeColeccionesHandler handlerIrrestricta = new GetHechosDeColeccionesHandler();

    @Override
    public void handle(@NotNull Context context) throws Exception {

        Integer idBuscado = context.pathParamAsClass("id", Integer.class).get();
        String modoVisualizacion = context.pathParam("modoVisualizacion");

        log.info("Listar hechos colección idColeccion={} modo={}", idBuscado, modoVisualizacion);

        FiltroHechoDTO filtro = FiltroHechosMapper.extraerFiltroDeContext(context);

        // ─────────────────────────────────────────────
        // 1) IRRESTRICTA → delega al handler que ya funciona
        // ─────────────────────────────────────────────
        if ("IRRESTRICTA".equalsIgnoreCase(modoVisualizacion)) {
            //handlerIrrestricta.handle(context);

            var opt = repoColecciones.findByIdFetchHechosYContribuyente(idBuscado); // <<-- NUEVO
            if (opt.isEmpty()) {
                log.warn("Colección no encontrada idColeccion={}", idBuscado);
                context.status(404).result("Colección no encontrada con ID: " + idBuscado);
                return;
            }

            Coleccion coleccion = opt.get();
            // Filtrar solo hechos activos (estado ACEPTADO)
            List<Hecho> hechosParaFiltrar = coleccion.getHechos().stream()
                    .filter(h -> h.getEstado() == core.models.entities.hecho.Estado.ACEPTADO)
                    .toList();
            List<Hecho> hechosFiltrados = FiltradorColecciones.getInstance()
                    .filtrarHechosPorDTO(hechosParaFiltrar, filtro);   // <-- filtrás esa lista

            var respuesta = hechosFiltrados.stream()
                    .filter(h -> h.getHash() != null)
                    .collect(Collectors.toMap(
                            Hecho::getHash,
                            Function.identity(),
                            (h1, h2) -> h1,          // si se repite el hash, me quedo con el primero
                            LinkedHashMap::new       // mantiene el orden
                    ))
                    .values()
                    .stream()
                    .map(HechoResumenDTO::from)
                    .toList();
            log.info("Hechos devueltos ok idColeccion={} modo=IRRESTRICTA count={}", idBuscado, respuesta.size());
            context.status(200).json(respuesta);

            return;
        }

        // ─────────────────────────────────────────────
        // 2) CURADA → devolver hechos visibles
        // ─────────────────────────────────────────────
        if ("CURADA".equalsIgnoreCase(modoVisualizacion)) {

            // Usamos el mismo repo que el otro handler (ya te trae hechos cargados)
            var opt = repoColecciones.findByIdFetchHechosVisiblesYContribuyente(idBuscado); // <<-- NUEVO
            if (opt.isEmpty()) {
                log.warn("Colección no encontrada idColeccion={}", idBuscado);
                context.status(404).result("Colección no encontrada con ID: " + idBuscado);
                return;
            }

            Coleccion coleccion = opt.get();
            //List<Hecho> hechosFiltrados = FiltradorColecciones.getInstance().filtrarColeccion(coleccion, criterios);
            if(coleccion.getAlgoritmoConsenso() == null){
                log.warn("Colección sin algoritmo de consenso idColeccion={}", idBuscado);
                context.status(400).result("La coleccion no tiene algoritmo de consenso definido.");
                return;
            }

            // devuelve DTOs para no tocar relaciones LAZY de Hecho!!

            List<Hecho> hechosParaFiltrar = coleccion.getHechosVisibles(); // <-- la lista de visibles
            List<Hecho> hechosFiltrados = FiltradorColecciones.getInstance()
                    .filtrarHechosPorDTO(hechosParaFiltrar, filtro);

            var respuesta = hechosFiltrados.stream()
                    .map(HechoResumenDTO::from)
                    .toList();
            log.info("Hechos devueltos ok idColeccion={} modo=CURADA count={}", idBuscado, respuesta.size());
            context.status(200).json(respuesta);
            return;
        }

        // ─────────────────────────────────────────────
        // 3) Modo inválido
        // ─────────────────────────────────────────────
        log.warn("modoVisualizacion inválido idColeccion={} modo={}", idBuscado, modoVisualizacion);
        context.status(400).result("modoVisualizacion inválido. Use IRRESTRICTA o CURADA.");
    }
}
