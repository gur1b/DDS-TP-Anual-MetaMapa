package core.api.handlers.solicitudesDeEliminacion;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.entities.solicitud.SolicitudDeEliminacion;
import core.models.repository.HechosRepository;
import core.models.repository.SolicitudEliminacionRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostAceptarSolicitudHandler implements Handler {


    private static final Logger log = LoggerFactory.getLogger(PostAceptarSolicitudHandler.class);

    private final SolicitudEliminacionRepository repo = SolicitudEliminacionRepository.getInstance();
    private final HechosRepository repoHechos = HechosRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        int id = context.pathParamAsClass("id", Integer.class).get();
        log.info("Aceptar solicitud id={}", id);

        SolicitudDeEliminacion solicitud = repo.findById(id);
        if (solicitud == null) {
            log.warn("Solicitud no encontrada id={}", id);
            context.status(404).result("Solicitud no encontrada");
            return;
        }

        solicitud.aceptarSolicitud();
        repo.update(solicitud);

        var hecho = solicitud.getHecho();
        if (hecho != null) {
            hecho.desactivarse();
            repoHechos.update(hecho);
            log.info("Solicitud aceptada: hecho desactivado idSolicitud={} hechoHash={}",
                    id, hecho.getHash());
        }

        context.status(200).result("Solicitud aprobada");

    }
}

