package core.api.handlers.hechos;

import core.models.repository.CategoriaRepository;
import core.models.entities.hecho.Categoria;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class GetCategoriasHandler implements Handler {
    private final CategoriaRepository repoCategorias = CategoriaRepository.getInstance();

    @Override
    public void handle(@NotNull Context ctx) {
        var nombres = repoCategorias.obtenerTodas()
                .stream()
                .map(Categoria::getNombre)
                .toList();
        ctx.json(nombres);
    }

}