package core.api.handlers.hechos;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.entities.hecho.Hecho;
import core.models.repository.HechosRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class GetHechoIdHandler implements Handler {

    private static final Logger log = LoggerFactory.getLogger(GetHechoIdHandler.class);
    private final HechosRepository repoHechos = HechosRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {

        Integer idBuscado = context.pathParamAsClass("id", Integer.class).get();
        log.info("Consulta hecho por id id={}", idBuscado);

        final Optional<Hecho> resultadoBusqueda = repoHechos.obtenerTodas().stream()
                .filter(m -> m.getId() == idBuscado)
                .findFirst();
        if (resultadoBusqueda.isPresent()) {
            log.info("Hecho encontrado id={}", idBuscado);
            context.status(200).json(resultadoBusqueda.get());
        } else {
            log.warn("Hecho no encontrado id={}", idBuscado);
            context.status(404);
        }
    }
}