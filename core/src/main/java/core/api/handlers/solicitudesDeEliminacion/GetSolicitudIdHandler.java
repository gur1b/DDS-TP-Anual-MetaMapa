package core.api.handlers.solicitudesDeEliminacion;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.entities.solicitud.SolicitudDeEliminacion;
import core.models.repository.SolicitudEliminacionRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class GetSolicitudIdHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(GetSolicitudIdHandler.class);

    private final SolicitudEliminacionRepository repoSolicitud = SolicitudEliminacionRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {

        Integer idBuscado = context.pathParamAsClass("id", Integer.class).get();
        log.info("Consulta solicitud por id id={}", idBuscado);
        final Optional<SolicitudDeEliminacion> resultadoBusqueda = repoSolicitud.obtenerTodas().stream()
                .filter(m -> m.getId() == idBuscado)
                .findFirst();
        if (resultadoBusqueda.isPresent()) {
            log.info("Solicitud encontrada id={}", idBuscado);
            context.status(200).json(resultadoBusqueda.get());
        } else {
            log.warn("Solicitud no encontrada id={}", idBuscado);
            context.status(404);
        }
    }

}
