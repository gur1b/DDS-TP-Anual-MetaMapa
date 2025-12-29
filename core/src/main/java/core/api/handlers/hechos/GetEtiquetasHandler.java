package core.api.handlers.hechos;

import core.models.entities.hecho.Categoria;
import core.models.entities.hecho.Etiqueta;
import core.models.repository.EtiquetasRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class GetEtiquetasHandler implements Handler {

    private final EtiquetasRepository repoEtiquetas = EtiquetasRepository.getInstance();

    @Override
    public void handle(@NotNull Context ctx) {

        var nombres = repoEtiquetas.obtenerTodas()
                .stream()
                .map(Etiqueta::getNombre)
                .toList();
        ctx.json(nombres);
    }
}
