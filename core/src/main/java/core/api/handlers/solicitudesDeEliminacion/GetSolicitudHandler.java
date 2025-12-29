package core.api.handlers.solicitudesDeEliminacion;

import core.api.DTO.SolicitudConHechoDTO;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.repository.SolicitudEliminacionRepository;
import org.jetbrains.annotations.NotNull;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetSolicitudHandler implements Handler {

    private static final Logger log = LoggerFactory.getLogger(GetSolicitudHandler.class);
    private final SolicitudEliminacionRepository repoSolicitudes =
            SolicitudEliminacionRepository.getInstance();

    @Override
    public void handle(@NotNull Context ctx) {
        log.info("Listar solicitudes (con hecho y contribuyente)");
        try {
            var dtos = repoSolicitudes.obtenerTodasConHechoYContribuyente()
                    .stream()
                    .map(SolicitudConHechoDTO::from)
                    .toList();

            log.info("Solicitudes listadas ok count={}", dtos.size());
            ctx.json(dtos);

        } catch (Exception e) {
            log.error("Error listando solicitudes", e);
            ctx.status(500).result("Error al listar solicitudes");
        }
    }
    }

