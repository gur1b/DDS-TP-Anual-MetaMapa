package core.api.handlers.colecciones;

import core.api.DTO.ActualizarConsensoDTO;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.entities.colecciones.Coleccion;
import core.models.repository.ColeccionesRepository;
import core.models.repository.FuentesRepository;
import org.jetbrains.annotations.NotNull;


public class PatchAlgoritmoDeConsenso implements Handler {

    private final ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();
    private final FuentesRepository fuentesRepository = FuentesRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        int idColeccion = Integer.parseInt(context.pathParam("id"));
        ActualizarConsensoDTO dto = context.bodyAsClass(ActualizarConsensoDTO.class);

        Coleccion coleccion = coleccionesRepository.getColeccion(idColeccion);
        if (coleccion == null) {
            context.status(404).result("Colección no encontrada");
            return;
        }

        coleccion.cambiarAlgoritmoConsenso(dto.tipoConsenso);
        coleccionesRepository.update(coleccion);

        context.status(200).result("Algoritmo de consenso actualizado correctamente");
    }

}
