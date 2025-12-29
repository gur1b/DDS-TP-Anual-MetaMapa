package core.api.handlers.colecciones;

import core.models.entities.colecciones.Coleccion;
import core.models.entities.colecciones.SchedulerActualizarVisibles;
import core.models.agregador.SchedulerAgregador;
import core.models.repository.ColeccionesRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EjecutarServicioHandler implements Handler {

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        SchedulerAgregador agregador = new SchedulerAgregador(false);
        SchedulerActualizarVisibles scheduler = new SchedulerActualizarVisibles();

        agregador.verificarNuevosHechos();
        scheduler.ejecutar();

        ctx.status(200).result("{\"message\": \"Cálculo de consenso ejecutado para todas las colecciones\"}");
    }
}