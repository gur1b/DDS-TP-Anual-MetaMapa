package core.api.graphql;

import core.models.entities.hecho.Hecho;
import core.models.repository.HechosRepository;
import core.models.repository.ColeccionesRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class HechosDataFetcher implements DataFetcher<List<Hecho>> {

    private final HechosRepository hechosRepository;
    private final ColeccionesRepository coleccionesRepository;

    public HechosDataFetcher(HechosRepository hechosRepository,
                             ColeccionesRepository coleccionesRepository) {
        this.hechosRepository = hechosRepository;
        this.coleccionesRepository = coleccionesRepository;
    }

    @Override
    public List<Hecho> get(DataFetchingEnvironment env) {

        Map<String, Object> filter = env.getArgument("filter");

        // Si el cliente no envía filtros, devolvés algo razonable (ej: todos ACEPTADOS)
        Stream<Hecho> stream = hechosRepository.listarHechosParaGraphQL().stream();

        if (filter != null) {

            // 1) Filtro por colección
            if (filter.get("coleccionId") != null) {
                Integer coleccionId = Integer.valueOf((String) filter.get("coleccionId"));
                var coleccion = coleccionesRepository.buscarColeccionParaGraphQL(coleccionId);
                if (coleccion != null) {
                    var idsHechosColeccion = coleccion.getHechos().stream()
                            .map(Hecho::getId)
                            .toList();

                    stream = stream.filter(h -> idsHechosColeccion.contains(h.getId()));
                }
            }

            // 2) Filtro por categoría (nombre)
            if (filter.get("categoriaNombre") != null) {
                String categoriaNombre = (String) filter.get("categoriaNombre");
                stream = stream.filter(h ->
                        h.getCategoria() != null &&
                                categoriaNombre.equalsIgnoreCase(h.getCategoria().getNombre())
                );
            }

            // 3) Filtro por estado
            if (filter.get("estado") != null) {
                String estadoStr = (String) filter.get("estado");
                stream = stream.filter(h -> h.getEstado().name().equals(estadoStr));
            }

            // 4) Filtro por fechas
            if (filter.get("sucesoDesde") != null) {
                LocalDate desde = LocalDate.parse((String) filter.get("sucesoDesde"));
                stream = stream.filter(h ->
                        h.getFechaSuceso() != null &&
                                !h.getFechaSuceso().isBefore(desde)
                );
            }
            if (filter.get("sucesoHasta") != null) {
                LocalDate hasta = LocalDate.parse((String) filter.get("sucesoHasta"));
                stream = stream.filter(h ->
                        h.getFechaSuceso() != null &&
                                !h.getFechaSuceso().isAfter(hasta)
                );
            }

            if (filter.get("cargaDesde") != null) {
                LocalDate desde = LocalDate.parse((String) filter.get("cargaDesde"));
                stream = stream.filter(h ->
                        h.getFechaCarga() != null &&
                                !h.getFechaCarga().isBefore(desde)
                );
            }
            if (filter.get("cargaHasta") != null) {
                LocalDate hasta = LocalDate.parse((String) filter.get("cargaHasta"));
                stream = stream.filter(h ->
                        h.getFechaCarga() != null &&
                                !h.getFechaCarga().isAfter(hasta)
                );
            }

            // 5) Filtro por idFuente / codigoFuente
            if (filter.get("idFuente") != null) {
                Integer idFuente = (Integer) filter.get("idFuente");
                stream = stream.filter(h -> h.getIdFuente() != null && h.getIdFuente().equals(idFuente));
            }
            if (filter.get("codigoFuente") != null) {
                String codigoFuente = (String) filter.get("codigoFuente");
                stream = stream.filter(h ->
                        h.getCodigoDeFuente() != null &&
                                h.getCodigoDeFuente().equalsIgnoreCase(codigoFuente)
                );
            }

            // 6) Búsqueda de texto
            if (filter.get("textoBuscado") != null) {
                String texto = ((String) filter.get("textoBuscado")).toLowerCase();
                stream = stream.filter(h ->
                        (h.getTitulo() != null && h.getTitulo().toLowerCase().contains(texto)) ||
                                (h.getDescripcion() != null && h.getDescripcion().toLowerCase().contains(texto))
                );
            }
        }

        return stream.toList();
    }
}
