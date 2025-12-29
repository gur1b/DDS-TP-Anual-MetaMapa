package core.api.handlers.colecciones;

import core.api.DTO.ColeccionDTO;
import core.models.entities.colecciones.ModoDeNavegacion;
import core.models.entities.colecciones.TipoConsenso;
import core.models.repository.*;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.entities.colecciones.Coleccion;
import core.models.entities.fuentes.Fuente;
import core.models.entities.hecho.Hecho;
import core.models.entities.hecho.Categoria;
import core.models.entities.hecho.Coordenadas;
import core.models.entities.colecciones.criterios.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PostColeccionHandler implements Handler {
    private final ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();
    private final HechosRepository hechosRepository = HechosRepository.getInstance();
    private final FuentesRepository fuentesRepository = FuentesRepository.getInstance();
    private final CategoriaRepository categoriaRepository = CategoriaRepository.getInstance();
    private final CoordenadasRepository coordenadasRepository = CoordenadasRepository.getInstance();
    private final CriteriosRepository criteriosRepository = CriteriosRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        Map<String, Object> dtoMap = context.bodyAsClass(Map.class);

        String titulo = (String) dtoMap.get("titulo");
        String descripcion = (String) dtoMap.get("descripcionColeccion");
        String identificadorHandle = (String) dtoMap.get("identificadorHandle");
        String modoDeNavegacion = (String) dtoMap.get("modoDeNavegacion");
        String algoritmoConsenso = (String) dtoMap.get("algoritmoConsenso");

        System.out.println("Creando coleccion: " + titulo);

        List<Hecho> hechosAsociados = new ArrayList<>();
        List<Hecho> hechosVisibles = new ArrayList<>();
        List<Fuente> fuentes = new ArrayList<>();

        // repositorios
        CriteriosRepository criteriosRepository = CriteriosRepository.getInstance();

        // Procesar hechos
        List<Integer> idHechos = (List<Integer>) dtoMap.getOrDefault("hechos", new ArrayList<>());
        for (Integer idHecho : idHechos) {
            if (idHecho == null) continue;
            Hecho hecho = hechosRepository.getHecho(idHecho);
            if (hecho == null) {
                context.status(404).result("Hecho con ID " + idHecho + " no encontrado");
                return;
            }
            hechosAsociados.add(hecho);
        }

        // Procesar fuentes
        List<Integer> idFuentes = (List<Integer>) dtoMap.getOrDefault("fuentes", new ArrayList<>());
        for (Integer idFuente : idFuentes) {
            if (idFuente == null) continue;
            Fuente fuente = fuentesRepository.getFuente(idFuente);
            if (fuente == null) {
                context.status(404).result("Fuente con ID " + idFuente + " no encontrada");
                return;
            }
            fuentes.add(fuente);
        }

        // Procesar criterios
        List<Criterio> criteriosDePertenencia = new ArrayList<>();
        List<Map<String, Object>> criteriosJSON = (List<Map<String, Object>>) dtoMap.getOrDefault("criterioDePertenencia", new ArrayList<>());

        for (Map<String, Object> criterioMap : criteriosJSON) {
            String type = (String) criterioMap.get("type");
            if (type == null) continue;

            Criterio criterio = null;

            switch (type) {
                case "nombre":
                    criterio = new CriterioNombre((String) criterioMap.get("palabraClave"));
                    break;
                case "descripcion":
                    criterio = new CriterioDescripcion((String) criterioMap.get("palabraClave"));
                    break;
                case "categoria":
                    String nombreCat = (String) criterioMap.get("categoria");
                    if (nombreCat != null && !nombreCat.isBlank()) {
                        Categoria cat = categoriaRepository.buscarPorNombre(nombreCat);
                        if (cat == null) {
                            cat = categoriaRepository.add(new Categoria(nombreCat));
                        }
                        criterio = new CriterioCategoria(cat);
                    }
                    break;
                case "ubicacion":
                    Double lat = ((Number) criterioMap.get("latitud")).doubleValue();
                    Double lon = ((Number) criterioMap.get("longitud")).doubleValue();
                    Coordenadas coords = coordenadasRepository.buscarPorCoordenadas(new Coordenadas(lat, lon));
                    if (coords == null) coords = new Coordenadas(lat, lon);
                    criterio = new CriterioUbicacion(coords);
                    break;
                case "fechaSuceso":
                    criterio = new CriterioFechaSuceso(
                            LocalDate.parse((String) criterioMap.get("desde")),
                            LocalDate.parse((String) criterioMap.get("hasta"))
                    );
                    break;
                case "fechaCarga":
                    criterio = new CriterioFechaCarga(
                            LocalDate.parse((String) criterioMap.get("desde")),
                            LocalDate.parse((String) criterioMap.get("hasta"))
                    );
                    break;
                case "horaSuceso":
                    criterio = new CriterioHoraSuceso(
                            LocalTime.parse((String) criterioMap.get("horaDesde")),
                            LocalTime.parse((String) criterioMap.get("horaHasta"))
                    );
            }

            if (criterio != null) {
                criteriosDePertenencia.add(criterio);
            }
        }

        Coleccion coleccion = new Coleccion(
                null,
                titulo,
                descripcion,
                criteriosDePertenencia,
                fuentes,
                hechosAsociados,
                hechosVisibles,
                identificadorHandle
        );

        if (modoDeNavegacion != null) {
            coleccion.setModoDeNavegacion(
                    switch (modoDeNavegacion.toUpperCase()) {
                        case "CURADA"     -> ModoDeNavegacion.CURADA;
                        case "IRRESTRICTA"-> ModoDeNavegacion.IRRESTRICTA;
                        default -> throw new IllegalArgumentException(
                                "Modo de navegacion desconocido: " + modoDeNavegacion
                        );
                    }
            );
        }

        if (algoritmoConsenso != null) {
            coleccion.cambiarAlgoritmoConsenso(
                    switch (algoritmoConsenso.toUpperCase()) {
                        case "ABSOLUTO", "ABSOLUTA" -> TipoConsenso.ABSOLUTO;
                        case "MAYORIA_SIMPLE", "MAYORIA-SIMPLE" -> TipoConsenso.MAYORIA_SIMPLE;
                        case "MULTIPLES_MENCIONES", "MULTIPLES-MENCIONES" -> TipoConsenso.MULTIPLES_MENCIONES;
                        case "SIN" -> null;
                        default -> throw new IllegalArgumentException(
                                "Tipo de algoritmo desconocido: " + algoritmoConsenso
                        );
                    }
            );
        }

        System.out.println(coleccion);
        coleccionesRepository.add(coleccion);
        context.status(201);
    }
}

