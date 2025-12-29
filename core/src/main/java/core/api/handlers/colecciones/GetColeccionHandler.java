package core.api.handlers.colecciones;

import core.models.repository.ColeccionesRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetColeccionHandler implements Handler{

    private static final Logger log = LoggerFactory.getLogger(GetColeccionHandler.class);
    private final ColeccionesRepository repoColecciones = ColeccionesRepository.getInstance();
    @Override
    public void handle(@NotNull Context ctx) {
        log.info("Listar colecciones");
        try {
            var dtos = repoColecciones.listarColeccionesDTOConCantidadHechos();
            log.info("Colecciones listadas ok count={}", dtos.size());
            ctx.status(200).json(dtos);

        } catch (Exception e) {
            log.error("Error listando colecciones", e);
            ctx.status(500).result("Error al listar colecciones");
        }
    }
}