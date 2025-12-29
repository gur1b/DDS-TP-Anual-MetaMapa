package core.api.handlers.fuentes;

import core.models.repository.FuentesRepository;
import core.models.repository.SolicitudEliminacionRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetFuentesHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(GetFuentesHandler.class);

    private final FuentesRepository fuentesRepository = FuentesRepository.getInstance();

    @Override
    public void handle(@NotNull Context ctx) {
        log.info("Listar fuentes");
        var dtos = fuentesRepository.obtenerTodas()
                .stream()
                .map(core.api.DTO.FuenteDTO::from)
                .toList();
        log.info("Fuentes listadas ok count={}", dtos.size());
        ctx.json(dtos);
    }
}