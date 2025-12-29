package core.api.configs;

import core.api.handlers.colecciones.GetColeccionHandler;
import core.api.handlers.colecciones.GetColeccionIdHandler;
import core.api.handlers.colecciones.GetHechosDeColeccionCurados;
import core.api.handlers.colecciones.GetHechosDeColeccionesHandler;
import core.api.handlers.hechos.GetCategoriasHandler;
import core.api.handlers.hechos.GetEtiquetasHandler;
import core.api.handlers.hechos.GetHechoIdHandler;
import core.api.handlers.hechos.PostHechoHandler;
import core.api.handlers.solicitudesDeEliminacion.GetSolicitudHandler;
import core.api.handlers.solicitudesDeEliminacion.PostSolicitudHandler;
import core.api.handlers.usuario.GetUsuarioPorCorreoHandler;
import core.api.handlers.usuario.PostRegistrarUsuarioHandler;
import io.javalin.Javalin;
import io.javalin.http.Handler;

public class ApiMetaMapaConfig {
    public static void configurarEndpoints(Javalin app) {
        app.get("/core/api/public/colecciones/{id}/hechos", new GetHechosDeColeccionesHandler());
        app.post("/core/api/public/solicitudes", new PostSolicitudHandler());
        app.get("/core/api/public/colecciones", new GetColeccionHandler());
        app.get("/core/api/public/colecciones/{id}", new GetColeccionIdHandler());
        app.get("/core/api/public/solicitudes", new GetSolicitudHandler()); //para prueba solo
        app.get("/core/api/public/colecciones/{id}/{modoVisualizacion}/hechos", new GetHechosDeColeccionCurados());
        app.post("/core/api/public/hechos/reportar", new PostHechoHandler());
        app.get("/core/api/public/categorias", new GetCategoriasHandler());
        app.get("/core/api/public/etiquetas", new GetEtiquetasHandler());


        // Endpoints de usuario para login y registro
        app.get("/core/api/public/usuarios/buscar", new GetUsuarioPorCorreoHandler());
        app.post("/core/api/public/usuarios/registrar", new PostRegistrarUsuarioHandler());
    }
}
