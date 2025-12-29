package core.api.handlers.solicitudesDeEliminacion;

import core.api.DTO.SolicitudDeEliminacionDTO;
import core.models.agregador.DetectorDeSpam;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.entities.hecho.Hecho;
import core.models.entities.solicitud.SolicitudDeEliminacion;
import core.models.repository.HechosRepository;
import core.models.repository.SolicitudEliminacionRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostSolicitudHandler implements Handler {

    private static final Logger log = LoggerFactory.getLogger(PostSolicitudHandler.class);

    private final SolicitudEliminacionRepository repoSolicitudes = SolicitudEliminacionRepository.getInstance();
    private final DetectorDeSpam detectorDeSpam = DetectorDeSpam.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        SolicitudDeEliminacionDTO dto  = context.bodyAsClass(SolicitudDeEliminacionDTO.class);
        log.info("Creando solicitud de eliminación hechoID={} descLen={}",
                (dto != null ? dto.hecho : "null"),
                (dto != null && dto.descripcion != null ? dto.descripcion.length() : 0)
        );

        Hecho hecho = HechosRepository.getInstance().getHecho(dto.hecho);

        if (hecho == null) {
            log.warn("Hecho no encontrado para solicitud hechoHash={}", dto.hecho);
            context.status(404).result("Hecho con ID " + dto.hecho + " no encontrado");
            return;
        }

        SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion(
                hecho,
                dto.descripcion
        );

        log.info("Solicitud creada ok hechoHash={} hechoTitulo=\"{}\"",
                dto.hecho, safe(hecho.getTitulo()));

        validarNuevaSolicitud(solicitud);
        if (detectorDeSpam.esSpam(solicitud.getDescripcion()))
        {solicitud.rechazarSolicitud();
            log.info("Solicitud marcada como spam y rechazada hechoHash={} hechoTitulo=\"{}\"",
                    dto.hecho, safe(hecho.getTitulo()));}
        repoSolicitudes.add(solicitud);
        context.status(201);
    }

    private void validarNuevaSolicitud(SolicitudDeEliminacion solicitud) {
        if (solicitud.getDescripcion() == null) {
            throw new IllegalArgumentException("La descripcion es obligatoria, elegí otro");
        }
    }

    private String safe(String s) {
        if (s == null) return "-";
        return s.length() > 120 ? s.substring(0, 120) + "..." : s;
    }
}
