package core.api.handlers.colecciones;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.entities.colecciones.Coleccion;
import core.models.repository.ColeccionesRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class DeleteColeccionHandler implements Handler {

    private static final Logger log = LoggerFactory.getLogger(DeleteColeccionHandler.class);

    private final ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        Integer id = context.pathParamAsClass("id", Integer.class).get();
        log.info("Eliminar colección id={}", id);
        Optional<Coleccion> coleccionOpt = coleccionesRepository.obtenerTodas().stream()
                .filter(c -> c.getId() == id)
                .findFirst();

        if (coleccionOpt.isPresent()) {
            coleccionesRepository.delete(coleccionOpt.get());
            log.info("Colección eliminada ok id={}", id);
            context.status(200).result("Colección con ID " + id + " eliminada");
        } else {
            log.warn("Colección no encontrada id={}", id);
            context.status(404).result("Colección con ID " + id + " no encontrada");
        }
    }
}
