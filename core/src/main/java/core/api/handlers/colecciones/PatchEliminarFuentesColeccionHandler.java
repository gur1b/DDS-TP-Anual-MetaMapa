package core.api.handlers.colecciones;

import core.api.DTO.ActualizarFuentesColeccionDTO;
import core.api.DTO.ColeccionConFuentesDTO;
import core.api.DTO.FuenteDTO;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.entities.colecciones.Coleccion;
import core.models.entities.fuentes.Fuente;
import core.models.repository.ColeccionesRepository;
import core.models.repository.FuentesRepository;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class PatchEliminarFuentesColeccionHandler implements Handler {

    private final ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();
    private final FuentesRepository fuentesRepository = FuentesRepository.getInstance();
    @Override
    public void handle(@NotNull Context ctx) {
        int idColeccion = Integer.parseInt(ctx.pathParam("id"));
        ActualizarFuentesColeccionDTO dto = ctx.bodyAsClass(ActualizarFuentesColeccionDTO.class);

        //  Traer la colección con fuentes inicializadas
        var opt = coleccionesRepository.findByIdFetchFuentes(idColeccion);
        if (opt.isEmpty()) {
            ctx.status(404).result("Colección no encontrada");
            return;
        }

        Coleccion coleccion = opt.get();

        if (dto == null || dto.fuentes == null || dto.fuentes.isEmpty()) {
            ctx.status(400).result("Lista de fuentes vacía o inválida");
            return;
        }

        // Eliminar las fuentes solicitadas
        Set<Integer> idsAEliminar = new HashSet<>();
        for (Integer idFuente : dto.fuentes) {
            if (idFuente == null) continue;
            if (fuentesRepository.getFuente(idFuente) == null) {
                ctx.status(404).result("Fuente con ID " + idFuente + " no encontrada");
                return;
            }
            idsAEliminar.add(idFuente);
        }

        boolean huboCambios = coleccion.getFuentes().removeIf(f -> idsAEliminar.contains(f.getId()));

        if (huboCambios) {
            coleccionesRepository.update(coleccion);
        }

        // Devolver DTO completo de la colección con sus fuentes actuales
        var respuesta = ColeccionConFuentesDTO.from(coleccion);
        ctx.status(200).json(respuesta);
    }
}
