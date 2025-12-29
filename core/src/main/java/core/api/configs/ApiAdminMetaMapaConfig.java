package core.api.configs;

import core.api.handlers.colecciones.*;
import core.api.handlers.fuentes.DeleteFuenteHandler;
import core.api.handlers.fuentes.GetFuentesHandler;
import core.api.handlers.fuentes.PostFuenteCSVHandler;
import core.api.handlers.fuentes.PostFuenteHandler;
import core.api.handlers.hechos.*;
import core.api.handlers.solicitudesDeEliminacion.*;
import core.api.handlers.sugerenciasDeCambio.GetSugerenciaHandler;
import core.api.handlers.sugerenciasDeCambio.PostAceptarSugerenciaHandler;
import core.api.handlers.sugerenciasDeCambio.PostRechazarSugerenciaHandler;
import core.api.handlers.sugerenciasDeCambio.PostSugerenciaHandler;
import io.javalin.Javalin;

public class ApiAdminMetaMapaConfig {
    public static void configurarEndpoints(Javalin app) {
        app.get("/core/api/admin/hechos", new GetHechoHandler());
        app.get("/core/api/admin/hechos/{id}", new GetHechoIdHandler());
        app.post("/core/api/admin/hechos", new PostHechoHandler());
        app.get("core/api/admin/solicitudes", new GetSolicitudHandler());
        app.post("core/api/admin/solicitudes", new PostSolicitudHandler());
        app.get("core/api/admin/colecciones", new GetColeccionHandler());
        app.post("core/api/admin/colecciones", new PostColeccionHandler());
        app.get("core/api/admin/colecciones/{id}", new GetColeccionIdHandler());
        app.delete("core/api/admin/colecciones/{id}", new DeleteColeccionHandler());
        app.patch("core/api/admin/colecciones/{id}", new PatchColeccionHandler());
        app.post("/core/api/admin/solicitudes/{id}/aceptar", new PostAceptarSolicitudHandler());
        app.post("/core/api/admin/solicitudes/{id}/rechazar", new PostRechazarSolicitudHandler());
        app.get("/core/api/admin/solicitudes/{id}", new GetSolicitudIdHandler());
        app.patch("core/api/admin/colecciones/{id}/fuentes/agregar", new PatchAgregarFuentesColeccionHandler());
        app.patch("core/api/admin/colecciones/{id}/fuentes/eliminar", new PatchEliminarFuentesColeccionHandler());
        app.patch("core/api/admin/colecciones/{id}/consenso/modificar", new PatchAlgoritmoDeConsenso());
        app.get("/core/api/admin/fuentes", new GetFuentesHandler());

        app.patch("/core/api/admin/hechos/{hash}", new PatchHechoHandler());
        app.delete("/core/api/admin/hechos/{hash}", new DeleteHechoHandler());

        app.post("/core/api/admin/fuentes", new PostFuenteHandler());
        app.post("/core/api/admin/fuentes/{id}/csv", new PostFuenteCSVHandler());
        app.post("core/api/admin/fuentes/{id}/eliminar", new DeleteFuenteHandler());

        app.post("core/api/admin/ejecutarServicio", new EjecutarServicioHandler());


        app.get("core/api/admin/sugerencias", new GetSugerenciaHandler());
        app.post("core/api/admin/sugerencias", new PostSugerenciaHandler());
        app.post("/core/api/admin/sugerencias/{id}/aceptar", new PostAceptarSugerenciaHandler());
        app.post("/core/api/admin/sugerencias/{id}/rechazar", new PostRechazarSugerenciaHandler());
    }
}
