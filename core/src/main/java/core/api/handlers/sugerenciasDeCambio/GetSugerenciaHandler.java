package core.api.handlers.sugerenciasDeCambio;

import core.api.DTO.SolicitudConHechoDTO;
import core.api.DTO.SugerenciaConHechoDTO;
import core.api.handlers.solicitudesDeEliminacion.GetSolicitudHandler;
import core.models.repository.SolicitudEliminacionRepository;
import core.models.repository.SugerenciasDeCambioRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetSugerenciaHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(GetSugerenciaHandler.class);
    private final SugerenciasDeCambioRepository sugerenciasRepository = SugerenciasDeCambioRepository.getInstance();

    @Override
    public void handle(@NotNull Context ctx) {
        log.info("Listar sugerencias (con sus datos y los del hecho)");
        try {
            var dtos = sugerenciasRepository.obtenerTodasConTodoInicializado()
                    .stream()
                    .map(SugerenciaConHechoDTO::from)
                    .toList();

            log.info("Solicitudes listadas ok count={}", dtos.size());
            ctx.json(dtos);

        } catch (Exception e) {
            log.error("Error listando solicitudes", e);
            ctx.status(500).result("Error al listar solicitudes");
        }
    }
}
