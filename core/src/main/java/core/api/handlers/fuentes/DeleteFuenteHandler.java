package core.api.handlers.fuentes;

import core.models.agregador.ConfigLoader;
import core.models.repository.ColeccionesRepository;
import core.models.repository.FuentesRepository;
import core.models.repository.HechosRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DeleteFuenteHandler implements Handler {

    private static final Logger log = LoggerFactory.getLogger(DeleteFuenteHandler.class);

    private final HechosRepository hechosRepository = HechosRepository.getInstance();
    private final ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();
    private final FuentesRepository fuentesRepository = FuentesRepository.getInstance();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private static final String CARGADOR_ESTATICO_BASE_URL =
            ConfigLoader.getProperty("CargadorEstatico");   // ej: http://localhost:8085/cargadorEstatico
    private static final String CARGADOR_PROXY_BASE_URL =
            ConfigLoader.getProperty("CargadorProxy");      // ej: http://localhost:8084/cargadorProxy

    @Override
    public void handle(@NotNull Context context) throws Exception {
        Integer id = context.pathParamAsClass("id", Integer.class).get();
        log.info("Eliminar fuente id={}", id);

        boolean eliminadoEnEstatica = eliminarEnCargador(CARGADOR_ESTATICO_BASE_URL + "/eliminar/" + id);
        boolean eliminadoEnProxy   = eliminarEnCargador(CARGADOR_PROXY_BASE_URL   + "/eliminar/" + id);

        if (eliminadoEnEstatica || eliminadoEnProxy) {
            hechosRepository.eliminarHechosPorIdFuente(id);
            coleccionesRepository.eliminarFuenteDeTodasLasColecciones(id);
            fuentesRepository.deleteById(id);
            log.info("Fuente eliminada ok id={}",
                    id);
            context.status(200).result("Fuente con ID " + id + " eliminada");
        } else {
            log.warn("No se pudo eliminar fuente (no encontrada en cargadores) id={}", id);
            context.status(404).result("La fuente con ID " + id + " no pudo ser eliminada");
        }
    }

    /**
     * Envía un DELETE al cargador. Devuelve true si se eliminó (2xx),
     * false si es 404 o hubo error.
     */
    private boolean eliminarEnCargador(String url) {
        try {
            String traceId = MDC.get("traceId");

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(java.time.Duration.ofSeconds(10));

            if (traceId != null && !traceId.isBlank()) {
                builder.header("X-Trace-Id", traceId);
            }

            HttpRequest req = builder
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());

            int status = resp.statusCode();
            if (status / 100 == 2) {
                log.info("Eliminación en {} OK url={} status={}", url, status);
                return true;
            }
            if (status == 404) {
                log.warn("No encontrada en {} url={} status=404", url);
                return false;
            }

            log.warn("Error eliminando en {} url={} status={} body={}",
                     url, status, safe(resp.body()));
            return false;

        } catch (Exception e) {
            log.error("Excepción llamando a {} url={}", url, e);
            return false;
        }
    }

    private String safe(String s) {
        if (s == null) return "-";
        return s.length() > 200 ? s.substring(0, 200) + "..." : s;
    }
}
