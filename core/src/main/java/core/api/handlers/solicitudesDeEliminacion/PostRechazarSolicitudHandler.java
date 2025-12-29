package core.api.handlers.solicitudesDeEliminacion;

import core.models.repository.HechosRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.entities.solicitud.SolicitudDeEliminacion;
import core.models.repository.SolicitudEliminacionRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class PostRechazarSolicitudHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(PostRechazarSolicitudHandler.class);

    private final SolicitudEliminacionRepository repo = SolicitudEliminacionRepository.getInstance();
    private final HechosRepository repoHechos = HechosRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        int id = context.pathParamAsClass("id", Integer.class).get();
        log.info("Rechazar solicitud id={}", id);

        SolicitudDeEliminacion solicitud = repo.findById(id);
        if (solicitud == null) {
            log.warn("Solicitud no encontrada id={}", id);
            context.status(404).result("Solicitud no encontrada");
            return;
        }

        solicitud.rechazarSolicitud();
        repo.update(solicitud);

        repoHechos.update(solicitud.getHecho());
        log.info("Solicitud rechazada ok para hechoHash={}",
                (solicitud.getHecho() != null ? solicitud.getHecho().getHash() : "null")
        );
        context.status(200).result("Solicitud rechazada");
    }

}
