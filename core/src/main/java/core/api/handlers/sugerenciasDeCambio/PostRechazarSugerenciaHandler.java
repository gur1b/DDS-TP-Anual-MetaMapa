package core.api.handlers.sugerenciasDeCambio;

import core.api.handlers.solicitudesDeEliminacion.PostRechazarSolicitudHandler;
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

public class PostRechazarSugerenciaHandler implements Handler{
    private static final Logger log = LoggerFactory.getLogger(PostRechazarSugerenciaHandler.class);

    private final SugerenciasDeCambioRepository repo = SugerenciasDeCambioRepository.getInstance();
    private final HechosRepository repoHechos = HechosRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        int id = context.pathParamAsClass("id", Integer.class).get();
        log.info("Rechazar sugerencia id={}", id);

        SugerenciaDeCambio sugerencia = repo.findById(id);
        if (sugerencia == null) {
            log.warn("Sugerencia no encontrada id={}", id);
            context.status(404).result("Sugerencia no encontrada");
            return;
        }

        sugerencia.rechazarSugerencia();
        repo.update(sugerencia);

        log.info("Sugerencia rechazada ok para idSugerencia={}", sugerencia.getId());
        context.status(200).result("Sugerencia rechazada");
    }
}
