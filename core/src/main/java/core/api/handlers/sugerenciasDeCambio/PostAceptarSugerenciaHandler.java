package core.api.handlers.sugerenciasDeCambio;

import core.api.handlers.solicitudesDeEliminacion.PostAceptarSolicitudHandler;
import core.models.entities.hecho.Hecho;
import core.models.entities.hecho.SugerenciaDeCambio;
import core.models.entities.solicitud.SolicitudDeEliminacion;
import core.models.repository.HechosRepository;
import core.models.repository.SolicitudEliminacionRepository;
import core.models.repository.SugerenciasDeCambioRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostAceptarSugerenciaHandler implements Handler{
    private static final Logger log = LoggerFactory.getLogger(PostAceptarSugerenciaHandler.class);

    private final SugerenciasDeCambioRepository repo = SugerenciasDeCambioRepository.getInstance();
    private final HechosRepository repoHechos = HechosRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        int id = context.pathParamAsClass("id", Integer.class).get();
        log.info("Aceptar sugerencia id={}", id);

        SugerenciaDeCambio sugerencia = repo.getByIdConEtiquetas(id);
        if (sugerencia == null) {
            log.warn("Sugerencia no encontrada id={}", id);
            context.status(404).result("Sugerencia no encontrada");
            return;
        }

        Hecho hechoActualizado = sugerencia.aceptarSugerencia();
        repo.update(sugerencia);
        repoHechos.update(hechoActualizado);

        log.info("Sugerencia aceptada: hecho modificado idSolicitud={}", id);

        context.status(200).result("Sugerencia aprobada");

    }
}
